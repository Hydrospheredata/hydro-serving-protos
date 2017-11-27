# hydro-serving-protos
This repository constains `.proto` messages definitions that are commonly used in 
[hydro-serving](https://github.com/Hydrospheredata/hydro-serving)

Some Tensorflow `proto` messages were extracted from main repo and slightly modified to satisfy our needs.

## Build
Run
```sh
make [language]
```
to run codegen for specified languages. If `language` is omitted, then `make` builds bindings 
for every supported language.
