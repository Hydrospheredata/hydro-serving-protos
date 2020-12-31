INSTALL_PY_REQ = false
VERSION := $(shell cat version)
BASE_DIR := $(shell pwd)
PYTHON = python
PROTOC = protoc
PROTOS_PATH = src
PY_WORK_PATH = python-package
GRPC_FILES = $(shell find src -name '*.proto')


# Common
# ------

all: scala python java
clean: clean_scala clean_py clean_java
test: test_python test_scala

# Scala 
# -----

scala:
	cd scala-package && sbt +package

test_scala:
	cd scala-package && sbt compile test

scala_publish_local: scala
	cd scala-package && sbt +publishLocal

clean_scala:
	rm -rf scala-package/target

# Java 
# ----

java: java_build
	@echo Compiling java package

java_build:
	cd java-package && ./gradlew clean build

java_publish:
    cd java-package && ./gradlew uploadArchives

clean_java: 
	rm -rf java-package/build

# Python
# ------

python: python_wheel

python_wheel: python_grpc
	cd $(PY_WORK_PATH) && $(PYTHON) setup.py bdist_wheel

python_grpc: py_requirements
	$(PYTHON) -m grpc_tools.protoc -I $(PROTOS_PATH) --python_out=$(PY_WORK_PATH) --grpc_python_out=$(PY_WORK_PATH) $(GRPC_FILES)

py_requirements:
ifeq ($(INSTALL_PY_REQ), true)
	pip install -r python-package/requirements.txt
endif

test_python:
	cd python-package && $(PYTHON) setup.py test

clean_py:
	find python-package -name '*_pb2.py' -delete
	find python-package -name '*_pb2_grpc.py' -delete
	rm -rf python-package/build
	rm -rf python-package/dist
	rm -rf python-package/hydro_serving_grpc.egg-info
