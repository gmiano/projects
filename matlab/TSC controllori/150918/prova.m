%-------------------------------------------------------------------------
% Prova di esame 18 09 2015
%-------------------------------------------------------------------------
% Pulizia workspace di lavoro
clear all;
close all;
clc;

% S.I Unità di misura
m=1; Km=1000*m; mm=0.001*m; cm=0.01*m;
Kg=1; g=0.001*Kg; mg=0.001*g;
s=1; min=60*s; h=60*min;
g=9.81*m/s^2; % Forza di gravità
Kgf=g; %Kilogrammiforza
N=1; Nm=N*m; mNm=0.001*Nm; 
V=1;
rad=1;
gradi=pi*rad/180;
rpm=2*pi/60;
Amp=1; mAmp=0.001*Amp;
Ohm=V/Amp;
Henry=V*s/Amp; mHenry=0.001*Henry;

%% Punto 1
%Parametri costanti
b=0.0575*Kg*m^2/rad;
d=2.5185*Kg*m^2/(rad*s);
M=0.15*Kg;
l=0.5*m;
k=200*Nm/rad;
j=0.2156*Kg*m^2/rad;
t=1.2*Kg*m^2/(rad*s);

%Vedi SEA_NonLineare

%% Punto 2
tau_e=0.2*N*m;
q_e=acos(tau_e/(M*g*l));
theta_e=q_e+tau_e/k;
dx0=[0.05 0.01 0 0]';
Tf=20*s;
x_e=[q_e theta_e 0 0]';
x0=x_e+dx0;
sim('SEA_ConstIN',Tf);
subplot(2,1,1);
plot(time,q,'b');
hold on;
grid on;
plot(time,theta,'r--');
xlabel('time [s]');
ylabel('q, \theta');
legend('q','\theta');
subplot(2,1,2);
plot(time,q-theta);
hold on;
grid on;
xlabel('time [s]');
ylabel('q-\theta');

%% Punto 3
[A,B,C,D] = linmod('SEA_NonLineare',x_e,tau_e);
eigen = eig(A);
% Controllo il segno dell'autovalore più grande
eigen=sort(eigen);
if eigen(1)==0
    disp('Il sistema è semplicemente stabile');
elseif eigen(1)>0
    disp('Il sistema è instabile');
else
    disp('Il sistema è asintoticamente stabile');
end
%% Punto 4
CO = ctrb(A,B);
[j1,j2] = size(CO);
if j1 == rank(CO)
    disp('La matrice è completamente controllabile, è possibile realizzare il controllo');
else
    disp('Il sistema non può essere controllato');
    return;
end
Q = diag([10 10 0 0]);
R = 1;
[S,E,K] = care(A,B,Q,R);%% Soluzione della ARE
disp('Il vettore dei guadagni di Kalman risulta:')
K




