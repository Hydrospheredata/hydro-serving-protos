PYTHON = python
PROTOS_PATH = src/main/protobuf
PY_PB_PATH = target/python
PROTO_FILES := $(shell find src -name '*.proto')
GRPC_FILES = src/main/protobuf/runtime_service.proto

all: scala python

scala: target_dir
	sbt compile

python: py_dir
	protoc -I $(PROTOS_PATH) --python_out=$(PY_PB_PATH) $(PROTO_FILES)
	$(PYTHON) -m grpc_tools.protoc -I $(PROTOS_PATH) --python_out=$(PY_PB_PATH) --grpc_python_out=$(PY_PB_PATH) $(GRPC_FILES)

py_dir: target_dir
	if [ ! -d "$(PY_PB_PATH)" ]; then mkdir $(PY_PB_PATH); fi;

target_dir:
	if [ ! -d "target" ]; then mkdir target; fi;

init:
	git submodule update --init --recursive --depth=1
