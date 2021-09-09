<<<<<<< HEAD
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
=======
all: scala python
>>>>>>> b8b4b1ff4a86c81bc8d151194f522a5e9c487af8

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
	cd python-package && poetry build

python_grpc: py_requirements
	cd python-package && poetry run poe compile

py_requirements:
<<<<<<< HEAD
ifeq ($(INSTALL_PY_REQ), true)
	pip install -r python-package/requirements.txt
endif

test_python:
	cd python-package && $(PYTHON) setup.py test
=======
	cd python-package && poetry install

test: test-scala # test-python 
>>>>>>> b8b4b1ff4a86c81bc8d151194f522a5e9c487af8

clean_py:
	find python-package -name '*_pb2.py' -delete
	find python-package -name '*_pb2_grpc.py' -delete
	rm -rf python-package/build
	rm -rf python-package/dist
	rm -rf python-package/hydro_serving_grpc.egg-info
