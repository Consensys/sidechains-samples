#!/bin/bash

# This script partitions the terminal window using tmux,
# running as many instances of CMD_LINE as params are in PARAMS.
# Press Escape to exit everything.

ARGS="33 22 11"
CMD_LINE="scripts/run_node.js "

STANDOUT=`tput smso`
OFFSTANDOUT=`tput rmso`

IS_FIRST=1

for ARG in $ARGS
do
    FULL_CMD_LINE="$CMD_LINE $ARG"
    if (( $IS_FIRST == 1))
    then
      IS_FIRST=0
      tmux start-server \; set -g remain-on-exit on
      tmux new-session -s multichain -d "bash -c \"echo $STANDOUT Running '$FULL_CMD_LINE' $OFFSTANDOUT ; $FULL_CMD_LINE\""
      tmux bind-key -n Escape kill-session
      tmux set -g mouse on
    else
      tmux split-window -b -t 0 -d "bash -c \"echo $STANDOUT Running '$FULL_CMD_LINE' $OFFSTANDOUT ; $FULL_CMD_LINE\""
    fi
done
tmux select-layout even-vertical
tmux attach
