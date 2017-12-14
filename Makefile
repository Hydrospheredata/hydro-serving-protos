VERSION := $(shell cat version)
BASE_DIR := $(shell pwd)
PYTHON = python
PROTOS_PATH = src/main/protobuf
PY_WORK_PATH = target/python
PY_PB_PATH := $(PY_WORK_PATH)/hydro_serving_grpc
PROTO_FILES := $(shell find src -name '*.proto')
GRPC_FILES = src/main/protobuf/tf/api/prediction_service.proto

define python_setup_py
from setuptools import setup, find_packages
setup(
    name='hydro_serving_grpc',
    version='$(VERSION)',
    author='Hydrospheredata',
    author_email='info@hydrosphere.io',
    long_description='hydro-serving-protos',
    description='hydro-serving-protos',
    url='https://github.com/Hydrospheredata/hydro-serving-protos',
    packages=['hydro_serving_grpc/contract','hydro_serving_grpc/tf','hydro_serving_grpc/tf/api'],
    install_requires=['protobuf>=3.3.0','grpcio>=1.7.0'],
    zip_safe=True,
    license='Apache 2.0',
    classifiers=(
        'Natural Language :: English',
        'License :: OSI Approved :: Apache Software License',
        'Programming Language :: Python'
    ),
)
endef
export python_setup_py

define python_init_py
#
endef
export python_init_py

all: scala python

scala: target_dir
	sbt -DappVersion=$(VERSION) package

python: py_dir
	pip install -r requirements.txt
	protoc -I $(PROTOS_PATH) --python_out=$(PY_PB_PATH) $(PROTO_FILES)
	$(PYTHON) -m grpc_tools.protoc -I $(PROTOS_PATH) --python_out=$(PY_PB_PATH) --grpc_python_out=$(PY_PB_PATH) $(GRPC_FILES)
	echo "$$python_setup_py" > $(PY_WORK_PATH)/setup.py
	echo "$$python_init_py" > $(PY_PB_PATH)/__init__.py
	cd $(PY_WORK_PATH) && $(PYTHON) setup.py sdist


py_dir: target_dir
	if [ ! -d "$(PY_PB_PATH)" ]; then mkdir -p $(PY_PB_PATH); fi;

target_dir: clean
	if [ ! -d "target" ]; then mkdir target; fi;

clean:
	rm -rf target

init:
	git submodule update --init --recursive --depth=1
