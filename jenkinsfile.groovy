def repository = 'hydro-serving-protos'


def buildAndPublishReleaseFunction={
    sh "sudo pip3 install --upgrade pip"
    sh "sudo pip3 install -r ${env.WORKSPACE}/python-package/requirements.txt"
    sh "make PYTHON=python3 all"
    sh "make PYTHON=python3 test"

    def curVersion = getVersion()
    dir("${env.WORKSPACE}/scala-package") {
        sh "./sbt/sbt -DappVersion=${curVersion} 'set pgpPassphrase := Some(Array())' +publishSigned"
        sh "./sbt/sbt -DappVersion=${curVersion} 'sonatypeReleaseAll'"
    }

    sh 'sudo pip3 install twine'
    configFileProvider([configFile(fileId: 'PYPIDeployConfiguration', targetLocation: 'python-package/.pypirc', variable: 'PYPI_SETTINGS')]) {
        sh "twine upload --config-file ${env.WORKSPACE}/python-package/.pypirc -r pypi ${env.WORKSPACE}/python-package/dist/*"
    }
}

def buildFunction={
    sh "sudo pip3 install --upgrade pip"
    sh "sudo pip3 install -r ${env.WORKSPACE}/python-package/requirements.txt"
    sh "make PYTHON=python3 all"
    sh "make PYTHON=python3 test"
}

pipelineCommon(
        repository,
        false, //needSonarQualityGate,
        [""],
        {},//collectTestResults, do nothing
        buildAndPublishReleaseFunction,
        buildFunction,
        buildFunction
)
