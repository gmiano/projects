%% Dato un sistema SISO espresso come funzione di trasferimento calcola le matrici A, B, C, D
function [A,B,C,D] = FormaCanonicaControllo(Num, Den)
    % La matrice Den contiene i valori a0 a1 ... aN-1
    % Prendo i valori negati
    n = length(Den)-1;
    m = length(Num);
    A = [zeros(n-1,1) eye(n-1); -fliplr(Den(2:end))];
    B = zeros(n,1);
    B(n) = 1;
    C = zeros(1,n);
    for i=1:1:m
       C(i) = Num(m+1-i); 
    end
    D = 0;