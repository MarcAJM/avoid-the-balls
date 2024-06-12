# Avoid The Balls

This is my first game ever I actually put quite amount of effort in. The idea is simple, try to avoid the balls for as long as possible!

## Technical Overview
I coded this game in Java using JavaFX. Furthermore, I used a well-known architecture called Entity Component System (ECS) which is often used for games. I had some trouble implementing this architecture because there are not really concrete examples available online. In the end I managed to make it work fortunately.
For collision detection, I made a Sweep And Prune (SAP) algorithm to remove obvious non overlapping circles. After that, with the remaining circles that could potentially collide, the program does a very precise check and concludes whether it collides or not.
