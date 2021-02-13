#!/bin/sh

print_help() {
	echo "--start | -s : start the applications"
	echo "--stop | -x: stop the applications"
}

if [[ $1 == '--start' || $1 == '-s' ]]; then
	docker-compose --file=docker-compose-remote.yml up -d --remove-orphans
elif [[ $1 == '--stop' || $1 == '-x' ]]; then
	docker-compose down --remove-orphans
elif [[ $1 == '--help' || $1 == '-h' ]]; then
	print_help
else
	print_help
fi
