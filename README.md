# `sched-temporal-test`

Demo plugin used to determine the temporal order of tasks
in the Bukkit scheduler relative to events called. The
original question can be found [here](https://www.spigotmc.org/threads/order-of-execution-scheduled-tasks-vs-events.478943/).

# Implementation

Testing the temporal order between scheduler tasks and
events is actually deceptively difficult to accomplish
convincingly. The key difficulty is that it is really hard
to tell whether an event gets fired in a prior tick or if
it is actually processed by the server prior to the tick.
We cannot rely on an external reference because when that
is ticked simply adds another unknown into the equation.

What I do in `sched-temporal-test` is use the scheduler as
a reference. What this means is that when events are fired,
it only is observed from the point of view of the
scheduler. Because we know when the scheduler tasks run
relative to other scheduler tasks, and we know when tasks
get executed relative to when they were scheduled, this is
a very useful way to derive the original processing order.

A task runs each tick incrementing a counter. Since this is
quite fast, I only observe the counter value by printing
to the console once every second (20 ticks).

A listener handles PlayerInteractEvents. This event handler
runs a task (which executes on the next tick) that prints
the current tick BOTH at the time of call and the time of
execution.

I spam clicks in-game to produce PlayerInteractEvent events
and look for the time of execution to match up with when
the scheduler observes the counter. From there, it is
possible to work out when each task runs and when the
PlayerInteractEvent was called based on the time of
execution tick values. The time of scheduling tick value
can then be reversed to determine when the scheduler runs:
if this value matches with the actual tick, then the event
was called first. However, if this tick value is greater,
then we know the scheduler task must have run first in
order to increment the tick counter prior to the event
being handled. This is analyzed in full in the Discussion
section below.

# Building

```
git clone https://github.com/caojohnny/sched-temporal-test.git
cd sched-temporal-test
./gradlew jar
```

# Discussion

Some relevant output to the console has been pasted below:

```
[20:01:23] [Server thread/INFO]: [SchedTemporalTest] Listener: 2780 (now = 2780)
[20:01:23] [Server thread/INFO]: [SchedTemporalTest] Scheduler current tick = 2780
```

Right off the bat, we can tell that there are 3 relevant
ticking events:

  1. Tick #2779 start
  2. Tick #2780 start
  3. Tick #2781 start

Starting from the top, we know that the listener schedules
the task for the next tick. Since the tick number at which
that task runs is `now = 2780`, we know that the listener's
tick number must have been recorded between Tick #2779 and
Tick #2780 somewhere:

  1. Tick #2779 start
  2. Listener fires with Tick #2780
  3. Tick #2780 start
  4. Tick #2781 start

We know that the scheduler task will print the current
tick, which means that the scheduler's message must occur
sometime between Tick #2780 and Tick #2781:

  1. Tick #2779 start
  2. Listener fires with Tick #2780
  3. Tick #2780 start
  4. Scheduler increments tick 2780 -> 2781   
  5. Tick #2781 start

Somehow, the listener was called and records a tick number
of 2780 even though it was in Tick #2779. The only
explanation for this is that the scheduler must have run
before the listener was called in Tick #2779:

  1. Tick #2779 start
  2. Scheduler increments tick 2779 -> 2780   
  3. Listener fires with Tick #2780
  4. Tick #2780 start
  5. Scheduler increments tick 2780 -> 2781
  6. Tick #2781 start

Therefore, the scheduler runs before events are fired.
Don't worry, you don't even need to take my word for it.
You can find the patch where this is explicitly done
[here](https://hub.spigotmc.org/stash/projects/SPIGOT/repos/craftbukkit/browse/nms-patches/MinecraftServer.patch#576).
The scheduler ticks at virtually the beginning of the tick,
which means that pretty much everything else has to follow.

With that being said, this is not set in stone and may
change. Depending on this fact makes your code extremely
fragile and no contract explicitly says that this must hold
true. It leads to potential confusion - not everyone knows
that the scheduler runs before event handlers. Not every
event is called within the main thread either - some events
run asynchronously and are not explicitly tied to the tick.

# Credits

Built with [IntelliJ IDEA](https://www.jetbrains.com/idea/)
