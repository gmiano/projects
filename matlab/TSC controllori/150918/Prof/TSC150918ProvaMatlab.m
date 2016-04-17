clear all
close all
clc

% Definizione dei parametri del sistema
b = 0.0575;  % kg m^2/rad
d = 2.5185;  % Kg m^2/(rad s)
m = 0.15;    % kg
l = 0.5;     % m
k = 200; % Nm/rad
j = 0.2156; %Kg m^2/rad
t = 1.2; %Kg m^2/(rad s)
g = 9.81; %m/s^2

taue = 0.2 %Nm
Dx0 = [0.05 0.01 0 0]';


qe = acos(taue/(m*g*l))
thetae = qe + taue/k;
Xe = [qe,thetae,0,0]';
X0= Xe + Dx0;

%% Punto 1. Nel file simulink SEA_modelDEE

%% Punto 2. 
sim('SEA_model_ConstInput',20)

figure(1)
set(1,'Name','Risposta del sistema non controllato')
subplot(2,1,1)
plot(time,q)
hold on
plot(time,theta,'r--')
grid on
xlabel('t')
ylabel('y(t)')
legend('q','theta')

subplot(2,1,2)
plot(time,q-theta)
grid on
xlabel('t')
ylabel('q - theta')

%% Punto 3.
[A,B,C,D]=linmod('SEA_modelDEE',Xe,taue)
disp('Autovalori del sistema linearizzato:')
eig(A)
disp('Il sistema linearizzato presenta un autovalore a parte reale positiva, pertanto il punto di equilibrio scelto per il sistema non lineare sarà instabile')


%% Punto 4.
[j1,j2]=size(A);
CO = ctrb(A,B);

if (rank(CO)==j1)
    disp('Sistema completamente controllabile')
else
    disp('Sistema NON completamente controllabile')
    return % Per uscire dallo script    
end
Q = diag([10 10 0 0]);
R = 1;
[S,E,K] = care(A,B,Q,R);%% Soluzione della ARE
disp('Il vettore dei guadagni di Kalman risulta:')
K
sim('RegolazioneLQ_mdl',20)
figure(2)
set(2,'Name','Regolazione LQ')
subplot(2,1,1)
plot(time,y(:,1))
hold on
plot(time,y(:,2),'r--')
grid on
xlabel('t')
legend('q','theta')

subplot(2,1,2)
plot(time,u)
grid on
xlabel('t')
ylabel('u')% % Calcolo del guadagno della retroazione statica dello stato


%% Punto 5.
C1 = [1 -1 0 0] % Nuova matrice dell'uscita 
OB = obsv(A,C1);

if  (rank(OB)== j1)
    disp('Il sistema è completamente osservabile')
else
    disp('Il sistema non è completamente osservabile')
    return
end

Po = [-20 -20 -20 -20]; % essendo il polo più lento del sistema in -1.2
L = - acker(A',C1',Po)';

sim('RegolazioneLQ_observer_mdl',20)

figure(3) %Stima asintotica dello stato
set(3,'Name','Stima dello stato')
subplot(2,2,1)
plot(time, X(:,1),'r--')
hold on
plot(time, Xo(:,1))
grid on
xlabel('time')
ylabel('p_a')

subplot(2,2,2)
plot(time, X(:,2),'r--')
hold on
plot(time, Xo(:,2))
grid on
xlabel('time')
ylabel('X_r')

subplot(2,2,3)
plot(time, X(:,3),'r--')
hold on
plot(time, Xo(:,3))
grid on
xlabel('time')
ylabel('p_r')

subplot(2,2,4)
plot(time, X(:,4),'r--')
hold on
plot(time, Xo(:,4))
grid on
xlabel('time')
ylabel('X_{rp}')


figure(4)
set(4,'Name','Regolazione LQ con osservatore')
subplot(2,1,1)
plot(time,y(:,1))
hold on
plot(time,y(:,2),'r--')
grid on
xlabel('t')
legend('q','theta')

subplot(2,1,2)
plot(time,u)
grid on
xlabel('t')
ylabel('u')

%% Punto 6. 

sim('RegolazioneDinamicaSistemaNonLineare_mdl',20)
figure(5)
set(5,'Name','Sistema non lineare regolato')
subplot(2,1,1)
plot(time,q)
hold on
plot(time,theta,'r--')
grid on
xlabel('t')
ylabel('y(t)')
legend('q','theta')

subplot(2,1,2)
plot(time,q-theta)
grid on
xlabel('t')
ylabel('q - theta')
