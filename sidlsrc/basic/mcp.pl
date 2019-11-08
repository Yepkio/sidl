name(mcp).
/* children */
children([alice, bob, charly, david, eric]).
makedirty([]).
makedirty([C | Cs]):-
	create([dirty, C]),
	makedirty(Cs).	

/* State */
init([start]).

init([C], 0.0):-
	children(Cs),
	member(C, Cs).

/* hidden */
hidden([dirty, C], [C]):-
	player([C]).
	
/* Legal */   
legal([dirt]):-
	fact([start]).
	
legal([C]):-
	player([C]),
	not(fact([start])),
	not(fact([_, stepped])).

/* Switch */	
switch([dirt], [dirt | SCs]):-
	children(Cs),
	getsubset(SCs, Cs),
	not(SCs = []).
	
switch([C], [C, S]):-
	player([C]),
	member(S, [stay, step]).
	
/* Owned */
owned([dirt], equal(N)):-
	children(Cs),
	length(Cs, L),
	N is (2^L)-1.

owned([C], [C]):-
	player([C]).
	
/* Defaults */
	
default([C], [stay]):-
	player([C]).
	
/* Dos */	
	
do([ dirt | DCs ]):-
	fact([start]),
	delete([start]),
	makedirty(DCs).
	
do([_, stay]).
	
do([C, step]):-
	not(fact([start])),
	create([C, stepped]).

payoff([C], -1):-
	not(fact([start])),
	not(tocreate([C, stepped])).	

payoff([C], 100.0):-
	tocreate([C, stepped]),
	fact([dirty , C]).

payoff([C], -10000.0):-
	tocreate([C, stepped]),
	not(fact([dirty , C])).	