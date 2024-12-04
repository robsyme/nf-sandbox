# export NXF_PLUGINS_DEV=$PWD/plugins
#  ../nextflow/launch.sh "$@"


NXF_PLUGINS_DEV=$PWD/plugins nextflow "$@" -plugins nf-sandbox@1.0.0