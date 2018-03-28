import unittest

from hydro_serving_grpc import TensorShapeProto
from hydro_serving_grpc.tf.tensor import StringTensor, DoubleTensor, TypedTensor
from hydro_serving_grpc.tf.tensor_shape import shape_from_proto, shape_to_proto
import hydro_serving_grpc.tf.types_pb2 as dtypes


class TestShape(unittest.TestCase):
    def test_shape_from_none(self):
        self.assertEqual(shape_from_proto(None), None)

    def test_proto_from_none(self):
        self.assertEqual(shape_to_proto(None), None)

    def test_shape_from_proto(self):
        proto_shape = TensorShapeProto(
            dim=[
                TensorShapeProto.Dim(size=1),
                TensorShapeProto.Dim(size=2),
                TensorShapeProto.Dim(size=10)
            ]
        )
        shape = shape_from_proto(proto_shape)
        self.assertEqual(shape, [1, 2, 10])

    def test_proto_from_shape(self):
        shape = [-1, 2, 3, 4]
        shape = shape_to_proto(shape)
        expected_proto_shape = TensorShapeProto(
            dim=[
                TensorShapeProto.Dim(size=-1),
                TensorShapeProto.Dim(size=2),
                TensorShapeProto.Dim(size=3),
                TensorShapeProto.Dim(size=4)
            ]
        )
        self.assertEqual(shape, expected_proto_shape)


class TestTensor(unittest.TestCase):
    def tensor_case(self, data_type, data):
        tensor = TypedTensor.empty_tensor(data_type)
        tensor.data = data
        print(tensor)
        proto = tensor.to_proto()
        print(proto)
        self.assertEqual(tensor.data, TypedTensor.from_proto(proto).data)

    def test_double_tensor(self):
        self.tensor_case(dtypes.DT_DOUBLE, [1.0, 2.0])

    def test_string_tensor(self):
        self.tensor_case(dtypes.DT_STRING, ["Hello", "world"])

    def test_float_tensor(self):
        self.tensor_case(dtypes.DT_FLOAT, [3.0, 4.0])

    def test_bool_tensor(self):
        self.tensor_case(dtypes.DT_BOOL, [True, False])
