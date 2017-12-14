#!/usr/bin/env bash

VERSION=$(cat version)
BASE_DIR=$(pwd)
PYTHON=python
PROTOS_PATH=src
PY_WORK_PATH=python-package
PY_PB_PATH=$PY_WORK_PATH
PROTO_FILES=$(find src -name '*.proto')
GRPC_FILES=src/hydro_serving_grpc/tf/api/prediction_service.proto
[ -z "$SKIP_PYTHON_REQ" ] && SKIP_PYTHON_REQ="false"

CMD=$1

function clean {
    rm -rf target
}

function compilePython {
    mkdir -p $PY_PB_PATH

    if [ "$SKIP_PYTHON_REQ" == "true" ]; then
        pip install -r requirements.txt
    fi
    protoc -I $PROTOS_PATH --python_out=$PY_PB_PATH $PROTO_FILES
    $PYTHON -m grpc_tools.protoc -I $PROTOS_PATH --python_out=$PY_PB_PATH --grpc_python_out=$PY_PB_PATH $GRPC_FILES

    cd $PY_WORK_PATH && $PYTHON setup.py bdist_wheel && cd $BASE_DIR
}

function compileScala {
    cd scala-package
    ./sbt/sbt -Dsbt.override.build.repos=true -Dsbt.repository.config=project/repositories -DappVersion=$VERSION package
    cd $BASE_DIR
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