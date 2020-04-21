def repository = 'hydro-serving-protos'


def buildAndPublishReleaseFunction={
    configFileProvider([configFile(fileId: 'PYPIDeployConfiguration', targetLocation: 'python-package/.pypirc', variable: 'PYPI_SETTINGS')]) {
        sh """#!/bin/bash
            python3 -m venv venv
            source venv/bin/activate
            pip install wheel~=0.34.2
            pip install twine
            pip install -r ${env.WORKSPACE}/python-package/requirements.txt
            make PYTHON=python3 all
            make PYTHON=python3 test
            python -m twine upload --config-file ${env.WORKSPACE}/python-package/.pypirc -r pypi ${env.WORKSPACE}/python-package/dist/*
        """
    }

    def curVersion = getVersion()
    dir("${env.WORKSPACE}/scala-package") {
        sh "sbt -DappVersion=${curVersion} 'set pgpPassphrase := Some(Array())' +publishSigned"
        sh "sbt -DappVersion=${curVersion} 'sonatypeReleaseAll'"
    }
}

def buildFunction={
    sh """#!/bin/bash
        python3 -m venv venv
        source venv/bin/activate
        pip install wheel~=0.34.2
        pip install -r ${env.WORKSPACE}/python-package/requirements.txt
        make all
        make test
    """
}

pipelineCommon(
        repository,
        false, //needSonarQualityGate,
        [],
        {},//collectTestResults, do nothing
        buildAndPublishReleaseFunction,
        buildFunction,
        buildFunction
)
