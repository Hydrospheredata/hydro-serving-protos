#!/usr/bin/env bash

VERSION=$(cat version)
BASE_DIR=$(pwd)
PYTHON=python
PROTOS_PATH=src/main/protobuf
PY_WORK_PATH=target/python
PY_PB_PATH=$PY_WORK_PATH/hydro_serving_grpc
PROTO_FILES=$(find src -name '*.proto')
GRPC_FILES=src/main/protobuf/tf/api/prediction_service.proto

CMD=$1

function clean {
    rm -rf target
}

function compilePython {
mkdir -p $PY_PB_PATH
pip install -r requirements.txt
protoc -I $PROTOS_PATH --python_out=$PY_PB_PATH $PROTO_FILES
$PYTHON -m grpc_tools.protoc -I $PROTOS_PATH --python_out=$PY_PB_PATH --grpc_python_out=$PY_PB_PATH $GRPC_FILES
cat <<EOF > $PY_WORK_PATH/setup.py
from setuptools import setup, find_packages
setup(
    name='hydro_serving_grpc',
    version='$VERSION',
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
EOF

cat <<EOF > $PY_PB_PATH/__init__.py
#
EOF

cd $PY_WORK_PATH && $PYTHON setup.py sdist && cd $BASE_DIR
}

function compileScala {
    ./sbt/sbt -DappVersion=$VERSION package
}

clean
case $CMD in
  python)
    compilePython
    ;;

  scala)
    compileScala
    ;;

  all)
    compileScala
    compilePython
    ;;
  *)
   echo "Unsupported command: $1 {use [python|scala|all] }"
   exit 1
   ;;
esac