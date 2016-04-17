%% Risoluzione di un sistema lineare 
% La funzione riceve come parametri la matrice A rappresentante il sistema omogeneo
% Mentre b � un vettore di tipo colonna

function [x] = SistemaLineare(A, b)
% Devo effettuare una serie di controlli
% Calcolo la dimensione di A
[row, col] = size(A);
% Devo controllare la matrice A
% In particolare devo controllare se la matrice � quadrata e a rango pieno.
if(ne(row,col))
    disp('La matrice non � quadrata, non esiste un unica soluzione')
end
% Inoltre la soluzione esiste, � unica e calcolabile se le righe sono
% linearmente indipendenti, cio� se la matrice ha rango massimo, ovvero se
% il determinante � diverso da 0
if(det(A)==0)
    disp('Le colonne di A non sono linearmente indipendenti, il rango � 0, quindi non � possibile calcolare la soluzione')
end
% Se il sistema � omogeneo settare a 0 b
if(nargin == 1)
   disp('Si considera un sistema omogeneo.')
   b = zeros(col,1);
end
% Se arrivo qui posso calcolare la soluzione
% Ax = b quindi x = A'b
x = inv(A)*b;
