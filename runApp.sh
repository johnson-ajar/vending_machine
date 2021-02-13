#!/bin/sh

print_help() {
	echo "--start | -s : start the applications"
	echo "--stop | -x : stop the applications"
	echo "--remove | -r : removes the images forcefully"
}

if [[ $1 == '--start' || $1 == '-s' ]]; then
	docker-compose --file=docker-compose-remote.yml up -d --remove-orphans
elif [[ $1 == '--stop' || $1 == '-x' ]]; then
	docker-compose --file=docker-compose-remote.yml down --remove-orphans
elif [[ $1 == '--remove' || $1 == '-r' ]]; then
	docker rmi --force johnson2ajar/vending_machine:vending_ui
	docker rmi --force johnson2ajar/vending_machine:vending_service
elif [[ $1 == '--help' || $1 == '-h' ]]; then
	print_help
else
	print_help
fi
