/* game definition predicates*/
/* game(name of game) */
:- multifile game/1.
/* hidden(fact, player) */
:- multifile hidden/2.
/* legal(switchID) */
:- multifile legal/1.
/* owned(switchID, player OR distribution) */
:- multifile owned/2.
/* only for unlimited actions (not for nature)
   unlimited(switchID, Structure of constants or types)*/
:- multifile unlimited/2.
/* switch(ID, action) */
:- multifile switch/2.
/* default action
   default(switchID, action)*/
:- multifile default/2.
/* payoff(player,accountchange)*/
:- multifile payoff/2.


/*	Definitions of parts of a state  */
:- dynamic fact/1.
:- dynamic sidl_account/2.

/* to create in the next state */
:- dynamic tocreate/1.
/* to delete in the next state */
:- dynamic todelete/1.

/* submited action at a switch 
	does(ID, Action)  */
:- dynamic does/2.
:- dynamic sidl_done/2.


/* Initialize */
sidl_init:- 
    init(Fact), 
    not(fact(Fact)), 
    assert(fact(Fact)), 
    fail.
sidl_init:- 
    init(Player, Amount), 
    not(sidl_account(Player, Amount)),
	assert(sidl_account(Player, Amount)), 
	fail.
sidl_init.

/* Clean */
sidl_clean:- 
    retractall(fact(_)), 
    retractall(sidl_account(_,_)).

/* Players */
player(Player):- 
    sidl_account(Player, _).

/*	Transitions of states */
delete(Fact):- 
    fact(Fact), 
    not(todelete(Fact)),
	assert(todelete(Fact)).
	
create(Fact):- 
    not(fact(Fact)), 
    not(tocreate(Fact)),
	assert(tocreate(Fact)).

/* Revealing */
sidl_reveal(Fact, Player):- 
    fact(Fact), 
    not(hidden(Fact, Player)).

/* Revealing changes */
sidl_reveal(Fact, Player, -):- 
    todelete(Fact), 
    not(hidden(Fact, Player)).
sidl_reveal(Fact, Player, +):- 
    tocreate(Fact), 
    not(hidden(Fact, Player)).
    
/* legal switches belonging to nature 
   	ID -switch, Dist -distribution, Type -type */	
sidl_nature(ID, Dist, Type):- 
    legal(ID), 
    owned(ID, Dist), 
	not(player(Dist)), 
	sidl_disttype(Dist, Type).

/* different types for distributions */
sidl_disttype([_|_], list).
sidl_disttype(equal(_), equal).

/* reassign for a switch the submited action 
	ID - switch
	Action - action */	
sidl_toggle(ID, Action):- 
    does(ID, O), 
    !, 
	retract(does(ID, O)), 
	assert(does(ID, Action)),
	!.
	
/* reassign for a switch the submited action 
	ID - switch
	Action - action */		
sidl_toggle(ID, Action):- 
    assert(does(ID, Action)).
	
/* toggle action at position 
	toggleP(-ID, -Pos)
	ID - switch
	P - position */   
sidl_toggleP(ID, Pos):- 
    sidl_actionat(ID, Pos, Action), 
    sidl_toggle(ID, Action).

/* unlimited number of actions -> false */
sidl_actions(ID, _):- 
    unlimited(ID,_),
    !,
    fail.    
/* limited number of actions */    
sidl_actions(ID, Actions):- 
	findall(Action, switch(ID, Action), Actions),
	not(Actions=[]).
/* types and structure of unlimited actions */
sidl_structures(ID, Structures):- 
	findall(Structure, unlimited(ID, Structure), Structures),
	not(Structures=[]).	

/* action at position */
sidl_actionat(ID, Pos, Action):- 
    sidl_actions(ID, Actions), 
    nth0(Pos, Actions, Action).

/* Submit an action 
    Player - player, ID - switch, C - Action                */
sidl_submit(Player, ID, Action):-
    legal(ID), 
    owned(ID, Player),
	switch(ID, Action), 
	!,
	sidl_toggle(ID, Action).

/* Terminal state */
sidl_terminal:- 
	not(legal(_)).

/* Legal switches with their actions */
sidl_legals(ID, Actions):- 
    legal(ID), 
    sidl_actions(ID, Actions).

/* Legal switches owned by players with their actions */
sidl_legals(Player, ID, Actions):- 
    player(Player), 
    legal(ID),
	owned(ID, Player), 
	sidl_actions(ID, Actions).
	
/* unlimited structures */
sidl_legalstructures(Player, ID, Structures):-
   player(Player),
   legal(ID),
   owned(ID, Player), 
   sidl_structures(ID, Structures).	

/* Get an action for a switch */
/* First try submit actions */
sidl_getanddo(ID, Action):- 
    does(ID, Action), 
    !, 
    do(Action), 
    !.

/* Then try default actions */
sidl_getanddo(ID, Action):- 
    default(ID, Action), 
    !, 
    do(Action), 
    !.

/* And finally take the first action */
sidl_getanddo(ID, Action):- 
    switch(ID, Action), 
    !, 
    do(Action), 
    !.
    
/* prepare */ 
sidl_prepare:- 
    legal(ID), 
    sidl_getanddo(ID, Action),
    assert(sidl_done(ID, Action)), 
    fail.
sidl_prepare:- 
    sidl_account(Player, Amount), 
    payoff(Player, Payoff),
	Newamount is Amount + Payoff, 
	retract(sidl_account(Player, Amount)),
	assert(sidl_account(Player, Newamount)), 
	fail.
sidl_prepare.

/* complete */   
sidl_complete:- 
    todelete(Fact), 
    retract(fact(Fact)),
	retract(todelete(Fact)), 
	fail.
sidl_complete:- 
    tocreate(Fact), 
    assert(fact(Fact)),
	retract(tocreate(Fact)), 
	fail.
sidl_complete:- 
    retractall(does(_, _)), 
    retractall(sidl_done(_, _)). 

/* prepare and complete */
sidl_chronon:- 
	sidl_prepare, 
	sidl_complete.
	 
	