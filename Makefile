all: scala python

scala:
	cd scala-package && sbt +package

scala_publish_local: scala
	cd scala-package && sbt +publishLocal

python: python_wheel

python_wheel: python_grpc
	cd python-package && poetry build

python_grpc: py_requirements
	cd python-package && poetry run poe compile

py_requirements:
	cd python-package && poetry install

test: test-scala # test-python 

test-scala:
	cd scala-package && sbt compile test

clean: clean_scala clean_py

clean_py:
	find python-package -name '*_pb2.py' -delete
	find python-package -name '*_pb2_grpc.py' -delete
	rm -rf python-package/build
	rm -rf python-package/dist
	rm -rf python-package/hydro_serving_grpc.egg-info

clean_scala:
	rm -rf scala-package/target
