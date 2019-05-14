from io import BytesIO
from itertools import chain
from functools import reduce

import hydro_serving_grpc as hs
from google.protobuf import text_encoding

from hydro_serving_grpc.timemachine.reqstore_service_pb2_grpc import *
from hydro_serving_grpc.timemachine.reqstore_service_pb2 import *
from hydro_serving_grpc.monitoring.metadata_pb2 import ExecutionError
import requests


class ReqstoreHttpClient:
    def __init__(self, host_and_port:str):
        self.host_and_port = host_and_port


    def getRange(self, from_ts, to_ts,  folder:str, limit, reverse):
        url = u'{0}/{1}/get'.format(self.host_and_port, folder)
        r = requests.get(url, stream=True, params={"from": from_ts, "to": to_ts, "maxMessages": limit, "reverse": reverse})
        content = r.content
        records = BinaryHelper.decode_records(content)
        return records


class ReqstoreClient:

    def __init__(self, host_and_port:str, insecure):
        self.host_and_port = host_and_port

        opt = options=[('grpc.max_send_message_length', 1024*1024*1024),
                       ('grpc.max_receive_message_length', 1024*1024*1024)]

        if(insecure):
            self.channel = grpc.insecure_channel(host_and_port, opt)
        else:
            creds = grpc.ssl_channel_credentials()
            self.channel = grpc.secure_channel(host_and_port, creds, opt)

        self.stub = TimemachineStub(self.channel)

    def save(self, folder:str, data:bytes, useWAL:bool = False):
        req = SaveRequest()
        req.folder = folder
        req.useWAL = useWAL
        req.data = data
        return self.stub.Save(req)

    def get(self, folder:str, timestamp, unique):
        req = GetRequest(
            timestamp = timestamp,
            unique = unique,
            folder = folder
        )

        data = self.stub.Get(req)

        return TsRecord(
            ts = data.id.timestamp,
            entries = BinaryHelper.decode_entry(data.id.unique, data.data)
        )


    def getRange(self, from_ts, to_ts,  folder:str):
        req = RangeRequest(
            till = to_ts,
            folder = folder
        )

        setattr(req, 'from', from_ts)

        iter = self.stub.GetRange(req)

        for data in iter:
            yield TsRecord(
            ts = data.id.timestamp,
            entries = BinaryHelper.decode_entry(data.id.unique, data.data)
        )


class BinaryHelper:

    @staticmethod
    def read_int(binary: BytesIO):
        return int.from_bytes(binary.read(4), byteorder='big')

    @staticmethod
    def read_long(binary: BytesIO):
        return int.from_bytes(binary.read(8), byteorder='big')

    @staticmethod
    def decode_entry(unique, binary):
        entries = []
        entry = Entry(unique, binary)
        entries.append(entry)
        return entries

    @staticmethod
    def read_message(binary: BytesIO, grpc_msg):
        header = binary.read(1) 
        data = b'' if header == 0 else binary.read()
        grpc_msg.ParseFromString(data)
        return grpc_msg

    @staticmethod
    def decode_records(data: bytes):
        bio = BytesIO(data)
        size = len(data)
        records = []
        while size > 0:
            ts = BinaryHelper.read_long(bio)
            unique = BinaryHelper.read_long(bio)
            length = BinaryHelper.read_int(bio)
            body = bio.read(length)
            entries = BinaryHelper.decode_entry(unique, body)
            records.append(TsRecord(ts, entries))
            size = size - length - 4 - 8 - 8
        return records

    @staticmethod
    def decode_request(data: bytes):
        return BinaryHelper.read_message(BytesIO(data), hs.PredictRequest())

    @staticmethod
    def decode_response(data: bytes):
        bio = BytesIO(data)
        offset = BinaryHelper.read_int(bio)
        respData = BytesIO(bio.read())

        if offset == 2:
            error = BinaryHelper.read_message(respData, ExecutionError())
            return error
        elif offset == 3:
            result = BinaryHelper.read_message(respData, hs.PredictResponse())
            return result
        raise UnicodeDecodeError

class Entry:
    def __init__(self, uid, data):
        self.uid = uid
        self.binary = data
        self.__request = None
        self.__response = None

    @property
    def request(self):
        if not self.__request:
            self._read_binary()
        return self.__request

    @property
    def response(self):
        if not self.__response:
            self._read_binary()
        return self.__response

    def _read_binary(self):
        bio = BytesIO(self.binary)

        request_size = BinaryHelper.read_int(bio)
        response_size = BinaryHelper.read_int(bio)
        self.__request = BinaryHelper.decode_request(bio.read(request_size))
        self.__response = BinaryHelper.decode_response(bio.read(response_size))

    def __repr__(self):
        return "Entry(uid={})".format(self.uid)


class TsRecord:
    def __init__(self, ts, entries):
        self.ts = ts
        self.entries = entries

    def __repr__(self):
        return "TsRecord(ts={}, entries={})".format(self.ts, self.entries)
