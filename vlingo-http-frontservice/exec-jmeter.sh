#!/bin/bash
JVM_ARGS="-Xms1024m -Xmx1024m" jmeter -n -t src/test/throughput.jmx -l target/throughput.jtl
