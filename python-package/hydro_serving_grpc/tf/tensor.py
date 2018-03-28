from hydro_serving_grpc import TensorProto
import hydro_serving_grpc.tf.types_pb2 as dtypes
from hydro_serving_grpc.tf.tensor_shape import shape_to_proto, shape_from_proto


class TypedTensor:
    def __init__(self, data, shape):
        if data is not None and not isinstance(data, list):
            raise TypeError()

        if shape is not None and not isinstance(shape, list):
            raise TypeError()

        self.data = data
        self.shape = shape

    def get_data(self):
        return self.data

    def set_data(self, new_data):
        self.data = new_data

    def get_proto_data(self, tensor):
        pass

    def dtype(self):
        pass

    def __str__(self):
        return "{} shape:{} data:{}".format(type(self), self.shape, self.data)

    def to_proto(self):
        tensor = TensorProto(
            dtype=self.dtype(),
            tensor_shape=shape_to_proto(self.shape)
        )
        proto_data = self.get_proto_data(tensor)
        proto_data.extend(self.get_data())
        return tensor

    @staticmethod
    def from_proto(tensor_proto):
        tensor_buf = TypedTensor.empty_tensor(tensor_proto.dtype)
        data = tensor_buf.get_proto_data(tensor_proto)
        tensor_buf.set_data(data)
        tensor_buf.shape = shape_from_proto(tensor_proto.tensor_shape)
        return tensor_buf

    @staticmethod
    def empty_tensor(data_type):
        if data_type == dtypes.DT_STRING:
            return StringTensor(None, None)
        elif data_type == dtypes.DT_DOUBLE:
            return DoubleTensor(None, None)
        elif data_type == dtypes.DT_FLOAT:
            return FloatTensor(None, None)
        elif data_type == dtypes.DT_MAP:
            return MapTensor(None, None)
        elif data_type == dtypes.DT_BOOL:
            return BoolTensor(None, None)
        elif data_type == dtypes.DT_COMPLEX64:
            return Complex64Tensor(None, None)
        elif data_type == dtypes.DT_COMPLEX128:
            return Complex128Tensor(None, None)
        elif data_type == dtypes.DT_INT8:
            return Int8Tensor(None, None)
        elif data_type == dtypes.DT_INT16:
            return Int16Tensor(None, None)
        elif data_type == dtypes.DT_INT32:
            return Int32Tensor(None, None)
        elif data_type == dtypes.DT_INT64:
            return Int64Tensor(None, None)
        elif data_type == dtypes.DT_UINT8:
            return Uint8Tensor(None, None)
        elif data_type == dtypes.DT_UINT16:
            return Uint16Tensor(None, None)
        elif data_type == dtypes.DT_UINT32:
            return Uint32Tensor(None, None)
        elif data_type == dtypes.DT_UINT64:
            return Uint64Tensor(None, None)
        else:
            raise ValueError("Unknown data type: {}".format(data_type))


class StringTensor(TypedTensor):
    def __init__(self, data, shape):
        super().__init__(data, shape)

    def set_data(self, new_data):
        self.data = [x.decode("utf-8") for x in new_data]

    def get_data(self):
        return [x.encode("utf-8") for x in self.data]

    def get_proto_data(self, tensor):
        return tensor.string_val

    def dtype(self):
        return dtypes.DT_STRING


class DoubleTensor(TypedTensor):
    def __init__(self, data, shape):
        super().__init__(data, shape)

    def get_proto_data(self, tensor):
        return tensor.double_val

    def dtype(self):
        return dtypes.DT_DOUBLE


class FloatTensor(TypedTensor):
    def __init__(self, data, shape):
        super().__init__(data, shape)

    def get_proto_data(self, tensor):
        return tensor.float_val

    def dtype(self):
        return dtypes.DT_FLOAT


class MapTensor(TypedTensor):
    def __init__(self, data, shape):
        super().__init__(data, shape)

    def get_proto_data(self, tensor):
        return tensor.map_val

    def dtype(self):
        return dtypes.DT_MAP


class BoolTensor(TypedTensor):
    def __init__(self, data, shape):
        super().__init__(data, shape)

    def get_proto_data(self, tensor):
        return tensor.bool_val

    def dtype(self):
        return dtypes.DT_BOOL


class Complex64Tensor(TypedTensor):
    def __init__(self, data, shape):
        super().__init__(data, shape)

    def get_proto_data(self, tensor):
        return tensor.scomplex_val

    def dtype(self):
        return dtypes.DT_COMPLEX64


class Complex128Tensor(TypedTensor):
    def __init__(self, data, shape):
        super().__init__(data, shape)

    def get_proto_data(self, tensor):
        return tensor.dcomplex_val

    def dtype(self):
        return dtypes.DT_COMPLEX128


class Int8Tensor(TypedTensor):
    def __init__(self, data, shape):
        super().__init__(data, shape)

    def get_proto_data(self, tensor):
        return tensor.int_val

    def dtype(self):
        return dtypes.DT_INT8


class Int16Tensor(TypedTensor):
    def __init__(self, data, shape):
        super().__init__(data, shape)

    def get_proto_data(self, tensor):
        return tensor.int_val

    def dtype(self):
        return dtypes.DT_INT16


class Int32Tensor(TypedTensor):
    def __init__(self, data, shape):
        super().__init__(data, shape)

    def get_proto_data(self, tensor):
        return tensor.int_val

    def dtype(self):
        return dtypes.DT_INT32


class Int64Tensor(TypedTensor):
    def __init__(self, data, shape):
        super().__init__(data, shape)

    def get_proto_data(self, tensor):
        return tensor.int64_val

    def dtype(self):
        return dtypes.DT_INT64


class Uint8Tensor(TypedTensor):
    def __init__(self, data, shape):
        super().__init__(data, shape)

    def get_proto_data(self, tensor):
        return tensor.uint32_val

    def dtype(self):
        return dtypes.DT_UINT8


class Uint16Tensor(TypedTensor):
    def __init__(self, data, shape):
        super().__init__(data, shape)

    def get_proto_data(self, tensor):
        return tensor.uint32_val

    def dtype(self):
        return dtypes.DT_UINT16


class Uint32Tensor(TypedTensor):
    def __init__(self, data, shape):
        super().__init__(data, shape)

    def get_proto_data(self, tensor):
        return tensor.uint32_val

    def dtype(self):
        return dtypes.DT_UINT32


class Uint64Tensor(TypedTensor):
    def __init__(self, data, shape):
        super().__init__(data, shape)

    def get_proto_data(self, tensor):
        return tensor.uint64_val

    def dtype(self):
        return dtypes.DT_UINT64
