import hydro_serving_grpc as hs
import unittest


class ImportTest(unittest.TestCase):
    def test_docs(self):
        hs
        print(dir(hs))
