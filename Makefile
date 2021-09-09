all: scala python

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
	cd python-package && poetry install

test: test-scala # test-python 

clean_py:
	find python-package -name '*_pb2.py' -delete
	find python-package -name '*_pb2_grpc.py' -delete
	rm -rf python-package/build
	rm -rf python-package/dist
	rm -rf python-package/hydro_serving_grpc.egg-info
