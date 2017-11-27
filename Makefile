PYTHON = python
PROTOC = protoc
PROTOS_PATH = src/main/protobuf
PY_PB_PATH = target/python

all: scala python

scala: target_dir
	sbt compile

python: py_dir runtime_service_grpc_pb.py model_schema_pb.py

.PRECIOUS: %_grpc_pb.py
%_grpc_pb.py: $(PROTOS_PATH)/%.proto
	$(PYTHON) -m grpc_tools.protoc -I $(PROTOS_PATH) --python_out=$(PY_PB_PATH) --grpc_python_out=$(PY_PB_PATH) $<

.PRECIOUS: %_pb.py
%_pb.py: $(PROTOS_PATH)/%.proto
	$(PROTOC) -I $(PROTOS_PATH) --python_out=$(PY_PB_PATH) $<

py_dir: target_dir
	if [ ! -d "$(PY_PB_PATH)" ]; then mkdir $(PY_PB_PATH); fi;

target_dir:
	if [ ! -d "target" ]; then mkdir target; fi;

init:
	git submodule update --init --recursive --depth=1
