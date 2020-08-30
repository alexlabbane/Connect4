# Connect 4 AI
This was an educational project with the goal of creating an AI for Connect 4 that can beat the average human player most of the time. More specifically, I wanted to gain some experience with game theory, the minimax algorithm, alpha-beta pruning, and other optimization techniques.

![Imgur Image](https://i.imgur.com/dU03ODj.png)

# Description
This Connect 4 AI works by searching through the game tree with the minimax algorithm to evaluate the best possible move. At its highest difficulty, it is able to search up to 11 plies into the tree (searching farther takes too long). Generally, this is enough for the AI to avoid falling into foolish traps, and it does appear to make "good" moves most of the time.

For more information regarding the minimax algorithm, see https://en.wikipedia.org/wiki/Minimax

# Optimizations
The Connect 4 game tree grows very quickly, so it was important to implement some optimizations that sped up the search. In general, when searching n moves ahead in the game tree, you can expect 7^n different states to evaluate. Even searching just 7 moves ahead, this number balloons past 800,000, which is far too many states to evaluate in a reasonable amount of time when trying to make a move.

The first optimization technique implemented was alpha-beta pruning. This optimization relies on the fact that in the minimax algorithm, we always assume that they opponent will select the best possible move (in order to "minimize" our score). This gives the algorithm an opportunity to stop exploring many paths in the game tree early because they could not effect the outcome of the final decision. For an in-depth explanation of alpha-beta pruning, see https://en.wikipedia.org/wiki/Alpha–beta_pruning. After implementing alpha-beta pruning, the AI could comfortably search up to 8 moves ahead.

The other main optimization applied to this project has to do with the representation of the board internally. The simplest way to represent the 7x6 board in Connect 4 was to use a 2-D array of game pieces, so this is what I started with. However, many operations are expensive when doing this (i.e. evaluating board states in minimax and checking for win conditions), so the decision was made to switch to a bitboard representation. This takes advantage of the fact that bitwise operations are implemented directly at the hardware level and are thus extremely fast to perform. Simply by using bit-shifts and other bitwise operations (XOR, AND, etc.) I am able to check for win conditions and score board states. This allowed the AI to search up to 11 moves ahead in the same amount of time as before. Since the number of states to evaluate grows exponentially as the search deepens, I calculate that the bitboard representation allowed for a ~300x improvement over the 2-D array implementation.

# Download
If you would like to play against the AI for yourself, you can download the latest release from https://github.com/alexlabbane/Connect4/releases
