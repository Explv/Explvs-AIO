# Explvs-AIO
This All-in-one(AIO) script is open source and free for all to use.

## CLI Usage example

Pass the name of your saved .config file as the script parameter:

`java -jar "OSBot 2.5.38.jar" -login osbot_user:osbot_passwd -bot osrs_user:osrs_passwd:pin -script "\"Explv's AIO v3.2\":example.config"`


## Development
Intellij is the recommended IDE of choice:[https://www.jetbrains.com/idea/download/](here)

Select `File -> New -> Project From Version Control -> Git`

Enter https://github.com/Explv/Explvs-AIO.git

And then follow the setup wizard.

If you're building the script for the first time, run: `mvn initialize`

This will download OSBot, and allow you to build the script.

To build the script, run: `mvn install`

The script will be built to your scripts directory `<home_dir>/OSBot/Scripts`

If / when OSBot updates, you will need to delete the `osbot.jar` that maven downloads, and re-run `mvn intialize` to get the latest version.

## Issues
If you find any bugs with the project, feel free to make a pull request fixing them. Alternatively, you can make an issue on this project and they will be addressed when possible.

## Colour Scheme

Grey: `#1b1919`

White: `#ffffff`

Blue: `#33b5e5`

## Icon templates

https://github.com/Explv/Explvs-AIO/tree/master/AIO/resources/images/templates
