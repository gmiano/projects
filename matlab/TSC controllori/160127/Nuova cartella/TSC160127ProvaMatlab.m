close all
clear all
clc

%% Definizione dei parametri
m  = 5;     %Kg
km = 5;     %N m^2 A^-2
k0 = 4;     %N m^2
kv = 1;     %V m^-1
v0 = 2;     % V
g = 9.81;   %m s^-2

% condizioni iniziali 
x0=0.5;
dx0=0;


%% Punto 1. 
% Modello nel file simulink Levitatore_mdl.mdl o Levitatore_dee_mdl.mdl (sono equivalenti)

%% Punto 2. linearizzazione
ie = sqrt((m*g*x0^2-k0)/km);
ue=ie;
Xe =[0.5 0]';
ye = kv * x0 + v0; % calcolo dell'uscita di equilibrio, si poteva fare anche così [Xe,ue,ye,DX]=trim('Levitatore_dee_mdl',[],ie,[],[],1,[])
[A,B,C,D]=linmod('Levitatore_dee_mdl',Xe,ue)

%% Punto 3. Stabilità del sistema linearizzato
disp('Gli autovalori della matrice di stato risultano:')
E = eig(A)
disp('pertanto il sistema in catena aperta risulta instabile')

%% Punto 5. Regolazione ottima
[order]=size(A);
CO = ctrb(A,B);
if (rank(CO)==order)
    Q = 1;
    R = 20;
    [K,S,E] = lqr(A,B,C'*Q*C,R)
else
    disp('Sistema NON completamente controllabile')
    return % Per uscire dallo script    
end

Xl0=[0.03;0];
Tfin = 2;
sim('SSmodel_LQ_regulator',Tfin)

figure
subplot(2,1,1)
plot(time,u)
ylabel('u(t)')
xlabel('time')
grid on
subplot(2,1,2)
plot(time,yl)
grid on
ylabel('y(t)')
xlabel('time')

%% Punto 5. Regolazione con osservatore dello stato

OB = obsv(A,C);
if (rank(OB)==order)
    Bu = B;
    Bw = B; % Il disturbo sul processo entra insieme all'ingresso di controllo
    d_w = 0.00005;
    d_y = 0.00001;
    W = d_w;      % = E{ww'},     
    V = d_y      % = E{vv'}
    % Filtro di Kalman: calcolo del guadagno
    Sys = ss(A, [Bu Bw],C,0);
    [KEST,LKalman,P] = kalman(Sys,W,V); % si può ottenere lo stesso risultato anche con la funzione kalman
    disp('Guadagni del filtro di Kalman:') 
    L = -LKalman; % Il segno meno serve per essere coerenti con lo schema a blocchi implementato
else
    disp('Sistema NON completamente osservabile')
    return % Per uscire dallo script    
end
Tfin = 2;
sim('SSmodel_LQG_regulator',Tfin)
figure
subplot(2,1,1)
plot(time,u)
ylabel('u(t)')
xlabel('time')
grid on
subplot(2,1,2)
plot(time,yl)
grid on
ylabel('y(t)')
xlabel('time')

figure %Stato vero e stato stimato
subplot(2,1,1)
plot(time,xl(:,1))
hold on
grid on
plot(time,xls(:,1),'r--')
ylabel('X(1)')
xlabel('time')
legend('stato vero','stato stimato')

subplot(2,1,2)
plot(time,xl(:,2))
hold on
grid on
plot(time,xls(:,2),'r--')
ylabel('X(2)')
xlabel('time')
legend('stato vero','stato stimato')

%% Punto 6. Regolazione del sistema non lineare nell'intorno del punto di
%% equilibrio
dx0 = 0;
x0 = 0.5 + 0.03; % Condizioni iniziali
Tfin = 10;
sim('Levitatore_LQG_regulator',Tfin)

figure
subplot(2,1,1)
plot(time,u)
ylabel('u(t)')
xlabel('time')
grid on
subplot(2,1,2)
plot(time,y)
grid on
ylabel('y(t)')
xlabel('time')
