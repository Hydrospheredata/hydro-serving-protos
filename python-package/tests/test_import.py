import hydro_serving_grpc as hsg
import unittest


class ImportTest(unittest.TestCase):
    def test_docs(self):
        print(dir(hsg))

    def test_onnx_import_check(self):
        a = hsg.onnx.TensorProto()
        print(type(a))