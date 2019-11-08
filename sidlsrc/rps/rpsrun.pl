name(rps).

beats(paper, rock).
beats(rock, G):-
    member(G, [scissors, monkey, banana]).
beats(scissors, paper).
beats(fire, G):-
    member(G, [rock, paper, scissors]).
beats(water, fire).
beats(G, water):-
    member(G, [rock, paper, scissors]).
beats(G1, G2):-
    append(_, [G1|[G2|_]], [paper, mosquito, hunter, monkey, paper]).
beats(monkey, banana).
beats(banana, hunter).
	
opponent(role1, role2).
opponent(role2, role1).

giventime(120).

gametype(1, [rock, paper, scissors], scissors, 3).
gametype(2, [hunter, monkey, paper, mosquito], hunter, 3).
gametype(3, [paper, rock, scissors,	fire, water], paper, 3).
gametype(4, [water, fire, paper, rock, monkey], water, 3).
gametype(5, [paper, rock, banana, hunter, monkey], paper, 3).
gametype(6, [banana, hunter, monkey, paper, mosquito], banana, 3).
gametype(7, [scissors, water, rock, monkey, paper, fire], scissors, 2).

nextgame(X, Y):-
    gametype(N, X, _, _), NPP is N + 1, gametype(NPP, Y, _, _).
    
gesture(P, G):-
    not(does([P], _)),
    fact([chosen, P, G]).
gesture(P, G):-
    does([P], [P, wait]),
    fact([chosen, P, G]).
gesture(P, G):-
    does([P], [P, G]).
    
regesture(P, G):-
    gesture(P, G).
regesture(P, G):-
    gesture(P, O),
    not(G = O),
    delete([chosen, P, O]),
    create([chosen, P, G]).

madegestures:-
	gesture(role1, G1),
	create([made, role1, G1]),
 	gesture(role2, G2),
	create([made, role2, G2]).
	
cleanmade:-
    delete([made, role1, _]),
    delete([made, role2, _]).
cleanmade.
 
incdraws:-
   	gesture(role1, G1),
	gesture(role2, G2),
	not(beats(G2, G1)),
	not(beats(G1, G2)),
	delete([draws, X]),
	XPP is X + 1,
	create([draws, XPP]). 
incdraws.
    
reround(X, NX):-
    delete([timer, 0]),
	delete([rounds, X]),
	create([rounds, NX]),
	giventime(T),
	create([timer, T]).
    
/* State */
init([gameon]).
init([game | Game]):-
    gametype(1, Game, _, _).
init([chosen, P, G]):-
	gametype(1, _, G, _),
    member(P, [role1, role2]).
init([rounds, X]):-
    gametype(1,_, _, X).
init([timer, X]):-
    giventime(X).
init([draws, 0]).

init([P], 0.0):- opponent(P, _).
    
/* hidden */
hidden([chosen, P, _], [O]):- opponent(P, O).
	
/* Legal */   
legal([P]):-
    fact([gameon]),
    player([P]).
legal([timer]):-
    fact([timer, X]),
    X > 0.
legal([round]):-
    fact([timer, 0]),
    fact([gameon]).
	
/* Switch */	
switch([P], [P, wait]):-
    player([P]).
switch([P], [P, G]):-
    player([P]),
	fact([game | Gs]),
	member(G, Gs).
switch([timer], [timer]).
switch([round], [round]).
	
/* Owned */
owned([P], [P]):- player([P]).
owned([timer], equal(1)).
owned([round], equal(1)).

/* Dos */	
do([_, wait]).
do([P, G]):-
    player([P]),
    fact([chosen, P, O]),
    not(O = G), 
	delete([chosen, P, O]),
	create([chosen, P, G]).
	
do([timer]):-
    fact([timer, X]),
    X > 0,
    XMM is X - 1,
    cleanmade,
    delete([timer, X]),
    create([timer, XMM]).
    
do([round]):-
    fact([rounds, X]),
    X > 1,
    XMM is X - 1,
    incdraws,
    reround(X, XMM),
    madegestures.
do([round]):-
    fact([rounds, 1]),
    fact([game | Game]),
    nextgame(Game, NGame),
    gametype(_, NGame, G, Rs),
    incdraws,
    reround(1, Rs),
    madegestures,
    delete([game | Game]),
    create([game | NGame]),
    regesture(role1, G),
    regesture(role2, G).	    
do([round]):-
    fact([rounds, 1]),
    fact([game | Game]),
    not(nextgame(Game, _)),
    incdraws,
    madegestures,
    delete([gameon]).

payoff([P], 1.0):-
    does([round], [round]),
	gesture(P, G1),
	opponent(P, O),
	gesture(O, G2),
	beats(G1, G2).
payoff([P], 0.0):-
    does([round], [round]),
	gesture(P, G1),
	opponent(P, O),
	gesture(O, G2),
	not(beats(G1, G2)).	