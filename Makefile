PYTHON = python
PROTOC = protoc
PROTOS_PATH = protobuf
TF_PATH = tensorflow
PY_PB_PATH = py_pb_src

python: py_dir runtime_service_grpc_pb.py model_schema_pb.py

.PRECIOUS: %_grpc_pb.py
%_grpc_pb.py: $(PROTOS_PATH)/%.proto
	$(PYTHON) -m grpc_tools.protoc -I $(PROTOS_PATH) -I $(TF_PATH) --python_out=$(PY_PB_PATH) --grpc_python_out=$(PY_PB_PATH) $<

.PRECIOUS: %_pb.py
%_pb.py: $(PROTOS_PATH)/%.proto
	$(PROTOC) -I $(TF_PATH) -I $(PROTOS_PATH) --python_out=$(PY_PB_PATH) $<

py_dir:
	if [ ! -d "$(PY_PB_PATH)" ]; then mkdir $(PY_PB_PATH); fi;

init:
	git submodule init
	git submodule update --depth=1
