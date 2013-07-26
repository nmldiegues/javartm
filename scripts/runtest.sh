#!/bin/bash

# this is specific to my machine, modify this accordingly
SDE_DIR=~/projects/intel-htm/sde-external-6.1.0-2013-07-22-lin/

$SDE_DIR/sde64 -rtm_mode full -tsx 1 -tsx_debug_log 1 -tsx_stats 1 -tsx_stats_call_stack 1 -tsx_cache_set_size 128 -- java javartm.Test
