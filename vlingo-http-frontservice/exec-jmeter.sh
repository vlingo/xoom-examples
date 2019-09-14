#!/bin/bash
JVM_ARGS="-Xms2048m -Xmx2048m" jmeter -n -t src/test/throughput.jmx -l target/throughput.jtl
