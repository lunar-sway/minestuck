
# Wave Function Collapse Implementation for Minestuck

## Concepts

### Cell

A space to fit a piece entry.

It corresponds to a position to place a structure piece, but it does not necessarily correspond to a structure piece.
A cell will hold no more than one structure piece, but a structure piece can cover multiple cells.

The cell size for the prototype structure is 8x8x8 minecraft blocks.

### Piece Entry

An option for what goes into a cell.

It consists of connector types for each of its six sides,
and potentially the constructor for a structure piece to be placed in the position of the cell.

A structure piece is represented by a piece entry for each cell that the structure piece, and for each relevant rotation.

### Connector Type

The type of a side of a piece entry.

Represented by a resource location name. Used to determine how piece entries may be adjacent to each other.

### Connection Tester

Determiner of if two connector types can connect.

Is built by pairing connector types that should connect together.

### Entry Palette

Consists of a list of piece entries weighted for random selection, as well as a connection tester.

### Entry Provider

A helper for generating piece entries for the entries data.

An entry provider can create piece entries for a structure template,
where it will look for marker blocks inside the template to determine the connector types of the created piece entries.

It can handle templates meant to span multiple cells, creating multiple piece entries accordingly.
But currently it only supports templates covering a rectangular shape of cells
(that is, all cells fit within the size of the template must be covered by the template).

An entry provider will also create the rotated variants of piece entries based on how the entry provider is configured.
(Rotation is supported, but mirroring is currently not supported.)

### Module

A 3D grid of cells that are generated together.

Modules are generated one by one independent of one another.
This way, modules can be generated in an arbitrary order without affecting the generated result.
This fits well in with Minecraft world generation, especially on the chunk and structure level of generation.

For adjacent modules to still connect, the border surrounding the module is generated first and then used to generate the rest of the module.
The border is generated under a seed and circumstances so that adjacent modules use the same border.

A module is generated using two entry palette.
A full palette is used for the inner area of the module, while a reduced palette is used for the border area.

A module corresponds to a StructureStart.

The grid size of modules for the prototype structure is 16x14x16 cells.
The size of modules for the prototype structure is therefore 128x112x128 blocks, or 8x8 chunks.
