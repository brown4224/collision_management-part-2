# collision_management
KSU CS 7827 class project for real time collision management

----------------------

<p><strong>SAMPLE OUTPUT</strong></p>

<p>A PUSHED: 0<br />PROCESS C STARTED</p>
<p>A PUSHED: 1<br />B pulled: 3<br />X : 0 : 0<br />Y : 0 : 2<br />Z : 3 : 6<br />A PUSHED: 2<br />B pulled: 3<br />X : 1 : 1<br />Y : 1 : 2<br />Z : 3 : 0<br />C pulled: 3<br />SECOND 2<br />X | 0 | 0<br />Y | 0 | 2<br />Z | 3 | 6</p>
<p>No collision at second 2</p>
<p>A PUSHED: 3<br />B pulled: 3<br />X : 2 : 2<br />Y : 2 : 2<br />Z : 3 : 1<br />C pulled: 3<br />SECOND 3<br />X | 1 | 1<br />Y | 1 | 2<br />Z | 3 | 0</p>
<p>No collision at second 3</p>
<p>B pulled: 3<br />A PUSHED: 4<br />X : 3 : 3<br />Y : 3 : 2<br />Z : 3 : 2<br />C pulled: 3<br />SECOND 4<br />X | 2 | 2<br />Y | 2 | 2<br />Z | 3 | 1</p>
<p>**** COLLISION between X and Y ****<br />Detected at second 4<br />Occurred at second 3)<br />Location (2, 2)</p>
<p>B pulled: 3<br />A PUSHED: 5<br />X : 4 : 4<br />Y : 4 : 2<br />Z : 3 : 3<br />C pulled: 3<br />SECOND 5<br />X | 3 | 3<br />Y | 3 | 2<br />Z | 3 | 2</p>
<p>**** COLLISION between Y and Z ****<br />Detected at second 5<br />Occurred at second 4)<br />Location (3, 2)</p>
<p>B pulled: 3<br />A PUSHED: 6<br />X : 5 : 5<br />Y : 5 : 2<br />Z : 3 : 4<br />C pulled: 3<br />SECOND 6<br />X | 4 | 4<br />Y | 4 | 2<br />Z | 3 | 3</p>
<p>No collision at second 6</p>
<p>B pulled: 3<br />A PUSHED: 7<br />X : 6 : 6<br />Y : 6 : 2<br />Z : 3 : 5<br />C pulled: 3<br />SECOND 7<br />X | 5 | 5<br />Y | 5 | 2<br />Z | 3 | 4</p>
<p>No collision at second 7</p>
<p>A PUSHED: 8<br />B pulled: 3<br />X : 7 : 0<br />Y : 7 : 2<br />Z : 3 : 6<br />C pulled: 3<br />SECOND 8<br />X | 6 | 6<br />Y | 6 | 2<br />Z | 3 | 5</p>
<p>No collision at second 8</p>
<p>B pulled: 3<br />A PUSHED: 9<br />X : 0 : 1<br />Y : 0 : 2<br />Z : 3 : 0<br />C pulled: 3<br />SECOND 9<br />X | 7 | 0<br />Y | 7 | 2<br />Z | 3 | 6</p>
<p>No collision at second 9</p>
<p>A PUSHED: 10<br />B pulled: 3<br />X : 1 : 2<br />Y : 1 : 2<br />Z : 3 : 1<br />C pulled: 3<br />SECOND 10<br />X | 0 | 1<br />Y | 0 | 2<br />Z | 3 | 0</p>
<p>No collision at second 10</p>
<p>A PUSHED: 11<br />B pulled: 3<br />X : 2 : 3<br />Y : 2 : 2<br />Z : 3 : 2<br />C pulled: 3<br />SECOND 11<br />X | 1 | 2<br />Y | 1 | 2<br />Z | 3 | 1</p>
<p>**** COLLISION between X and Y ****<br />Detected at second 11<br />Occurred at second 10)<br />Location (1, 2)</p>
<p>A PUSHED: 12<br />B pulled: 3<br />X : 3 : 4<br />Y : 3 : 2<br />Z : 3 : 3<br />C pulled: 3<br />SECOND 12<br />X | 2 | 3<br />Y | 2 | 2<br />Z | 3 | 2</p>
<p>No collision at second 12</p>
<p>A PUSHED: 13<br />B pulled: 3<br />X : 4 : 5<br />Y : 4 : 2<br />Z : 3 : 4<br />C pulled: 3<br />SECOND 13<br />X | 3 | 4<br />Y | 3 | 2<br />Z | 3 | 3</p>
<p>No collision at second 13</p>
<p>B pulled: 3<br />X : 5 : 6<br />Y : 5 : 2<br />Z : 3 : 5<br />A PUSHED: 14<br />C pulled: 3<br />SECOND 14<br />X | 4 | 5<br />Y | 4 | 2<br />Z | 3 | 4</p>
<p>No collision at second 14</p>
<p>A PUSHED: 15<br />B pulled: 3<br />X : 6 : 0<br />Y : 6 : 2<br />Z : 3 : 6<br />C pulled: 3<br />SECOND 15<br />X | 5 | 6<br />Y | 5 | 2<br />Z | 3 | 5</p>
<p>No collision at second 15</p>
<p>A PUSHED: 16<br />B pulled: 3<br />X : 7 : 1<br />Y : 7 : 2<br />Z : 3 : 0<br />C pulled: 3<br />SECOND 16<br />X | 6 | 0<br />Y | 6 | 2<br />Z | 3 | 6</p>
<p>No collision at second 16</p>
<p>A PUSHED: 17<br />B pulled: 3<br />X : 0 : 2<br />Y : 0 : 2<br />Z : 3 : 1<br />C pulled: 3<br />SECOND 17<br />X | 7 | 1<br />Y | 7 | 2<br />Z | 3 | 0</p>
<p>No collision at second 17</p>
<p>B pulled: 3<br />A PUSHED: 18<br />X : 1 : 3<br />Y : 1 : 2<br />Z : 3 : 2<br />C pulled: 3<br />SECOND 18<br />X | 0 | 2<br />Y | 0 | 2<br />Z | 3 | 1</p>
<p>**** COLLISION between X and Y ****<br />Detected at second 18<br />Occurred at second 17)<br />Location (0, 2)</p>
<p>B pulled: 3<br />A PUSHED: 19<br />X : 2 : 4<br />Y : 2 : 2<br />Z : 3 : 3<br />C pulled: 3<br />SECOND 19<br />X | 1 | 3<br />Y | 1 | 2<br />Z | 3 | 2</p>
<p>No collision at second 19</p>
<p>B pulled: 3<br />X : 3 : 5<br />A FINISHED - NO MORE INBOUND VALUES<br />Y : 3 : 2<br />Z : 3 : 4<br />C pulled: 3<br />B pulled: 3<br />X : 4 : 6<br />Y : 4 : 2<br />Z : 3 : 5<br />BufferAB completed<br />SECOND 20<br />X | 2 | 4<br />Y | 2 | 2<br />Z | 3 | 3</p>
<p>No collision at second 20</p>
<p>C pulled: 3<br />SECOND 21<br />X | 3 | 5<br />Y | 3 | 2<br />Z | 3 | 4</p>
<p>No collision at second 21</p>
<p>C pulled: 3<br />SECOND 22<br />X | 4 | 6<br />Y | 4 | 2<br />Z | 3 | 5</p>
<p>No collision at second 22</p>
<p>DONE. 4 collisions occurred over 20 seconds.<br />BufferCD completed<br />about to wait</p>
<p>PRESS ENTER TO EXIT</p>