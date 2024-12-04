include { reverse } from 'plugin/nf-hello'

workflow {
    Channel.reverse("hello from a plugin") | view()
}