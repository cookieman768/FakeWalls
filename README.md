# FakeWalls
Bukkit plugin: Allows server owners to create walls which players with associated access permissions can walk through like they aren't even there!

# Commands
* /fw - Top command; shows currently selected wall; returns false
* /fw create <name ...> - Creates a new wall with name
* /fw list - Lists all walls
* /fw reload - Reloads plugin configuration
* /fw remove - Removes selected wall
* /fw select <name ...> - Selects wall with name
* /fw type <material> - Sets the block type for wall

# Permissions
* fw.admin - Allows player to use "/fw" commands and the selection tool
* fw.access.<name> - Access permission for individual walls. Wall permission names have stripped colors, lowercase letters, and underscores in place of spaces. Ex. A wall named "&6A BIG Wall" would have an access permission of "fw.access.a_big_wall". Players with the permission will see air, while players without it will see the wall.

# Config
* VIEW_DISTANCE - How many block away walls can be viewed from. This is nothing more than a bounding box.

All plugin messages can be edited in config.yml.

# Side note
Walls need to be selected to change their type or remove them, and can be selected either through the command or by clicking on the wall with the selection tool (still a Wooden Shovel). All of the commands except for "/fw create" is open to CONSOLE.

# Credits
* Marenwynn for originally writing the plugin.
* Comphenix for ProtocolLib and his packet wrappers.
