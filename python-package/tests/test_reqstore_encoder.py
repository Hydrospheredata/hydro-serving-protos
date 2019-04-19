import unittest
from hydro_serving_grpc.timemachine.reqstore_client import *
import base64
import grpc
import numpy as np
import hydro_serving_grpc as hs


class TMTest(unittest.TestCase):
#
#     coded_string = 'FZI/124VNZwAAAAAAAAAAAAAACwAAAAVAAAADwEKEgoDd3RmEgIIARoHZmRmZGZkZgAAAAMBqgYHCgNrZXkSABWSP9dyhmkMAAAAAAAAAAEAAAAsAAAAFQAAAA8BChIKA3d0ZhICCAEaB2ZkZmRmZGYAAAADAaoGBwoDa2V5EgAVkj/XcrLGFAAAAAAAAAACAAAALAAAABUAAAAPAQoSCgN3dGYSAggBGgdmZGZkZmRmAAAAAwGqBgcKA2tleRIAFZI/13LXOhwAAAAAAAAAAwAAACwAAAAVAAAADwEKEgoDd3RmEgIIARoHZmRmZGZkZgAAAAMBqgYHCgNrZXkSABWSP9dzCEUwAAAAAAAAAAQAAAAsAAAAFQAAAA8BChIKA3d0ZhICCAEaB2ZkZmRmZGYAAAADAaoGBwoDa2V5EgAVkj/Xcy2UwAAAAAAAAAAFAAAALAAAABUAAAAPAQoSCgN3dGYSAggBGgdmZGZkZmRmAAAAAwGqBgcKA2tleRIAFZI/13NTSLQAAAAAAAAABgAAACwAAAAVAAAADwEKEgoDd3RmEgIIARoHZmRmZGZkZgAAAAMBqgYHCgNrZXkSABWSP9dzfEQcAAAAAAAAAAcAAAAsAAAAFQAAAA8BChIKA3d0ZhICCAEaB2ZkZmRmZGYAAAADAaoGBwoDa2V5EgAVkj/Xc6Wu2AAAAAAAAAAIAAAALAAAABUAAAAPAQoSCgN3dGYSAggBGgdmZGZkZmRmAAAAAwGqBgcKA2tleRIAFZI/13PJOUgAAAAAAAAACQAAACwAAAAVAAAADwEKEgoDd3RmEgIIARoHZmRmZGZkZgAAAAMBqgYHCgNrZXkSAA=='
#     decoded_string = base64.b64decode(coded_string)
#
    def test_e2e(self):
        client = ReqstoreHttpClient("https://dev.k8s.hydrosphere.io/reqstore")
        data = client.getRange(0, 1854897851804888100, "45", "40", "false")
        print(len(data))
        for record in data:
            for entry in record.entries:
                print(entry.response)
    #
#         client = ReqstoreClient("dev.k8s.hydrosphere.io:443", False)
#         folder = "newFolder"
#
#         ts = BinaryHelper.decode_records(self.decoded_string)
#
#         toSave = ts[0].entries[0].binary.read()
#
#         result = client.save(folder, toSave)
#         print('saved ({}, {}) to {}'.format(result.timestamp, result.unique, folder))
#         val = client.get(folder, result.timestamp, result.unique)
#         print('fetched: {}'.format(val))
#         data = client.getRange(0, 1854897851804888100, folder)
#
#         for item in data:
#             print(item)
#             for entry in item.entries:
#                 print(entry.request)
#                 print(entry.response)
#
