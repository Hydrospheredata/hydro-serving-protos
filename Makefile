INSTALL_PY_REQ = false
VERSION := $(shell cat version)
BASE_DIR := $(shell pwd)
PYTHON = python
PROTOC = protoc
PROTOS_PATH = src
PY_WORK_PATH = python-package
PROTO_FILES := $(shell find src -name '*.proto')
GRPC_FILES = $(shell find src -name '*_service.proto')


all: scala python

scala:
	cd scala-package && ./sbt/sbt -DappVersion=$(VERSION) package

python: python_wheel

python_wheel: python_grpc
	cd $(PY_WORK_PATH) && env VERSION=$(VERSION) $(PYTHON) setup.py bdist_wheel

python_grpc: python_proto | py_requirements
	$(PYTHON) -m grpc_tools.protoc -I $(PROTOS_PATH) --python_out=$(PY_WORK_PATH) --grpc_python_out=$(PY_WORK_PATH) $(GRPC_FILES)

python_proto:
	$(PROTOC) -I $(PROTOS_PATH) --python_out=$(PY_WORK_PATH) $(PROTO_FILES)

py_requirements:
ifeq ($(INSTALL_PY_REQ), true)
	pip install -r python-package/requirements.txt
endif

clean: clean_scala clean_py

clean_py:
	find python-package -name '*_pb2.py' -delete
	find python-package -name '*_pb2_grpc.py' -delete
	rm -rf python-package/build
	rm -rf python-package/dist
	rm -rf python-package/hydro_serving_grpc.egg-info

clean_scala:
	rm -rf scala-package/target
