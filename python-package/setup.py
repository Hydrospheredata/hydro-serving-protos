from setuptools import setup, find_packages

with open("../version") as v:
    version = v.read()
setup(
    name='hydro_serving_grpc',
    version=version,
    author='Hydrospheredata',
    author_email='info@hydrosphere.io',
    long_description='hydro-serving-protos',
    description='hydro-serving-protos',
    url='https://github.com/Hydrospheredata/hydro-serving-protos',
    packages=find_packages(),
    install_requires=['protobuf>=3.6.1', 'grpcio>=1.7.0'],
    zip_safe=True,
    license='Apache 2.0',
    classifiers=(
        'Natural Language :: English',
        'License :: OSI Approved :: Apache Software License',
        'Programming Language :: Python'
    ),
    test_suite='tests'
)
