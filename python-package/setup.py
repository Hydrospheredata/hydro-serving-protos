from setuptools import setup, find_packages

setup(
    name='hydro_serving_grpc',
    version='$VERSION',
    author='Hydrospheredata',
    author_email='info@hydrosphere.io',
    long_description='hydro-serving-protos',
    description='hydro-serving-protos',
    url='https://github.com/Hydrospheredata/hydro-serving-protos',
    packages=['hydro_serving_grpc/contract', 'hydro_serving_grpc/tf', 'hydro_serving_grpc/tf/api'],
    install_requires=['protobuf>=3.3.0', 'grpcio>=1.7.0'],
    zip_safe=True,
    license='Apache 2.0',
    classifiers=(
        'Natural Language :: English',
        'License :: OSI Approved :: Apache Software License',
        'Programming Language :: Python'
    ),
)
