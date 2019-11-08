name(nim).
init([alice, 10]).
init([alice], 0.0).
init([bob], 0.0).	
  
legal([main]) :-                 
  fact([_, I]),              
  I > 0.    
    
switch([main], [T]) :-
  fact([_, I]),
  M is min(I, 3),
  between(1, M, T). 
  
switch([main], [wait]).

owned([main], [A]):-
  fact([A, _]). 

default([main], [1]).

do([wait]).                
                               
do([T]):- 
  fact([A, I]),
  NI is I - T,
  player([B]),
  not(A = B),
  delete([A, I]),
  create([B, NI]).
  
payoff([A], 1.0):-
  tocreate([A, 0]).

payoff([A], -1.0):-
  tocreate([B, 0]),
  not(A = B).


  
  
  