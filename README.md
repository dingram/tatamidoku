# Tatamidoku

Tatamidoku is a fun game/logic puzzle I found via [Andrea Sabbatini's 100 Logic
Games][logic-games], though it is just named "Tatami" there. This is *not* the
same as [Tatamibari][tatamibari].

Unfortunately, the hint system in **100 Logic Games** is not ideal, simply
providing the answer for the topmost leftmost square. Instead, I decided to
build a generator and algorithmic solver, in order to provide more gentle hints
(e.g. "look at this column") and to create my own puzzles.

[logic-games]: http://www.andreasabbatini.com/LogicGames.aspx#LG1
[tatamibari]: https://en.wikipedia.org/wiki/Tatamibari

## Rule summary

Tatamidoku is played on a square grid, which is also divided into sections, a
little like [sudoku][sudoku]. However in Tatamidoku, the subdivisions are
rectangular and just a single cell wide or tall. Each section contains the same
number of cells, and the grid is at least two sections across. Common sizes
are 3-, 4-, or 5-cell sections, and 2- or 3-section grids.

This layout can produce an effect rather like [tatami mats][tatami] used for
flooring in traditional Japan, hence the name.

To be able to more easily explain the rest of the rules, let's assume that each
section has _n_ cells, and the grid is _s_ sections across, for a total of _g_
= _n_ × _s_ cells on each side.

The grid must be filled using the digits from 1 to _n_. Similar to sudoku, each
section must contain each digit exactly once. In addition to this, each row and
column must contain each digit _s_ times and cells that share a border must not
have the same digit (though it's permitted diagonally).

### Example

One of the most common layouts is 3-cell sections and 6x6 (i.e. 2-section)
grids. These sections must be filled with the digits 1, 2, and 3. Each of those
digits must appear twice in every row and every column, and orthogonally
adjacent cells must have different values.

For example, this completed grid:

```
┏━━━┳━━━┳━━━┳━━━┯━━━┯━━━┓
┃ 1 ┃ 2 ┃ 3 ┃ 2 │ 3 │ 1 ┃
┠───╂───╂───╊━━━┿━━━┿━━━┫
┃ 3 ┃ 1 ┃ 2 ┃ 1 │ 2 │ 3 ┃
┠───╂───╂───╊━━━┿━━━┿━━━┫
┃ 2 ┃ 3 ┃ 1 ┃ 3 │ 1 │ 2 ┃
┣━━━╋━━━╇━━━╇━━━╈━━━╈━━━┫
┃ 3 ┃ 2 │ 3 │ 1 ┃ 2 ┃ 1 ┃
┠───╊━━━┿━━━┿━━━╉───╂───┨
┃ 2 ┃ 1 │ 2 │ 3 ┃ 1 ┃ 3 ┃
┠───╊━━━┿━━━┿━━━╉───╂───┨
┃ 1 ┃ 3 │ 1 │ 2 ┃ 3 ┃ 2 ┃
┗━━━┻━━━┷━━━┷━━━┻━━━┻━━━┛
```

Notice how the sections don't necessarily align, possibly made clearer by
highlighting them with their direction:

```
┏━━━┳━━━┳━━━┳━━━┯━━━┯━━━┓
┃ | ┃ | ┃ | ┃ - │ - │ - ┃
┠───╂───╂───╊━━━┿━━━┿━━━┫
┃ | ┃ | ┃ | ┃ - │ - │ - ┃
┠───╂───╂───╊━━━┿━━━┿━━━┫
┃ | ┃ | ┃ | ┃ - │ - │ - ┃
┣━━━╋━━━╇━━━╇━━━╈━━━╈━━━┫
┃ | ┃ - │ - │ - ┃ | ┃ | ┃
┠───╊━━━┿━━━┿━━━╉───╂───┨
┃ | ┃ - │ - │ - ┃ | ┃ | ┃
┠───╊━━━┿━━━┿━━━╉───╂───┨
┃ | ┃ - │ - │ - ┃ | ┃ | ┃
┗━━━┻━━━┷━━━┷━━━┻━━━┻━━━┛
```

[sudoku]: https://en.wikipedia.org/wiki/Sudoku
[tatami]: https://en.wikipedia.org/wiki/Tatami
