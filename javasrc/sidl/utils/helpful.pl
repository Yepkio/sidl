
between(F, L, _):-
    L < F, !, false.
between(F, _, F).
between(F, L, N):-
    NF is F + 1,
    between(NF, L, N).

minmember([], 0).
minmember([X], X).
minmember([X | Xs], Z):-
    minmember(Xs, Y),
	Z is min(X, Y).

maxmember([], 0).
maxmember([X], X).
maxmember([X | Xs], Z):-
    maxmember(Xs, Y),
	Z is max(X, Y).	

getsubset(0, [], []):-!.
getsubset(1, [M], S):-
    member(M, S).
getsubset(N, S, S):-
	length(S, NL),
	N>=NL,
	!.
getsubset(N, [M | SS], S):-
    N > 0,
    NM is N - 1,
    getsubset(NM, SS, S),
    findall(I, (nth0(I, S, E),
    			member(E, SS)), Is),
    minmember(Is, Max),
    nth0(P, S, M),
    P<Max.

getsubset([],[]):-!.
getsubset([], _).
getsubset(SS, S):-
	member(M, S),
	nth1(I, S, M),
	getsubset(I, SS, S).

binomial(0, _, 0) :- !.
binomial(X, 1, X) :- !.
binomial(X, X, 1) :- !.
binomial(P, Q, R) :-
	P1 is P - 1,
	Q1 is Q - 1,
	binomial(P1, Q, R1),
	binomial(P1, Q1, R2),
	R is R1 + R2.
	