%% TEORIA DEI SISTEMI E DEL CONTROLLO
%% Soluzione del compito del 15 luglio 2015

clear all
close all
clc


%% Preambolo per la generazione dei dati utilizzati per l'identificazione

s = tf('s');
G = 5/(0.01*s+1)/(s^2/9+1.2/3*s+1);
Ts = 0.01;
Gd = c2d(G,Ts,'zoh')
[B,A] = tfdata(Gd,'v');
ti = 0:Ts:100;
ui = sin(0.1*ti)+sin(1*ti)+sin(10*ti);
sim('Impianto',100);
save TSC150715Dati  y u
clear all
clc
% Fine Preambolo per la creazione dei dati per il compito

%% Inizio del compito vero e proprio
load TSC150715Dati
Ts = 0.01;

%% Punto 1. Plottaggio dei dati
time = 0:Ts:Ts*(length(u)-1);
figure(1)
set(1,'name','Ingresso e uscita dell''impianto da identificare')
subplot(2,1,1)
plot(time,u)
grid on
xlabel('t')
ylabel('u(t)')
subplot(2,1,2)
plot(time,y)
grid on
xlabel('t')
ylabel('y(t)')


%% Punto 2. Identificazione del sistema
Q1 = [];
Q2 = [];
n = 3; %ordine del sistema
for i=0:length(y)-(n+1)
    Q1=[Q1; y(i+n:-1:i+1)'];
    Q2=[Q2; u(i+n:-1:i+1)'];
end
Q = [Q1, Q2];
y1 = y(n+1:end);

% Calcolo della pseudoinversa ottimizzata di matlab 
alpha = pinv(Q)*y1;

A1 = [1 -alpha(1:n)'];
B1 = [alpha(n+1:2*n)'];
G1 = tf(B1,A1,Ts)
[Y,T] = step(G1)
figure(2)
set(2,'name','Risposta al gradino del sistema identificato')
plot(T,Y)
grid on
xlabel('t')
ylabel('y(t)')

%% Punto 3. Rappresentazione del sistema nello spazio degli stati

[Az,Bz,Cz,Dz] = ssdata(G1)


%% Punto 4. Progettazione di un Controllore LQ con azione integrale
% Definizione del sistema (tempo-discreto) aumentato
Ai = [Az zeros(n,1); Ts*Cz 1]
Bi = [Bz; 0]
R = 1;
Q = 30;
qi = 500;% Peso sul'integrale dell'errore
Qi = [Cz'*Q*Cz zeros(n,1);  zeros(1,n) qi] % Definizione dei pesi per il sistema aumentato
[Si,Ei,Ki] = dare(Ai,Bi,Qi,R); % Guadagno di kalman per il sistema aumentato
K = Ki(1:n); % Guadagno sulla retroazione dello stato
H = Ki(n+1); % Guadagno sul ramo con l'integrale dell'errore
X0 = [0;0;0];
Tfin = 3;
yp = 12;
sim('ABCDDiscreteModel_OptimalControlIntegral_mdl',Tfin) % simulazione
figure(3)
set(3,'name','Sistema con controllo ottimo integrale')
subplot(2,1,1)
stairs(time,yd,'r:')
hold on
grid on
stairs(time,y)
ylabel('y(t)')
xlabel('t')
legend('y_p','y')
subplot(2,1,2)
stairs(time,u)
grid on
ylabel('u(t)')
xlabel('t')


%% Punto 5. Aggiunta di un osservatore dead-beat
L = -acker(Az',Cz',[0 0 0])' % Osservatore deadbeat
sim('ABCDDiscreteModel_OptimalControlIntegral_obs_mdl', Tfin)
figure(4)
set(4,'name','Sistema con controllo ottimo integrale e osservatore')
subplot(2,1,1)
stairs(time,yd,'r:')
hold on
grid on
stairs(time,y)
ylabel('y(t)')
xlabel('t')
legend('y_p','y')
subplot(2,1,2)
stairs(time,u)
grid on
ylabel('u(t)')
xlabel('t')

figure(5)
set(5,'name','Stima dello stato')
subplot(3,1,1)
stairs(time,Xs(:,1))
hold on
stairs(time,X(:,1),'r--')
grid on
xlabel('t [s]')
ylabel('x_1')
legend('x_{1s}','x_1')
subplot(3,1,2)
stairs(time,Xs(:,2))
hold on
stairs(time,X(:,2),'r--')
grid on
xlabel('t [s]')
ylabel('x_2')
legend('x_{2s}','x_2')
subplot(3,1,3)
stairs(time,Xs(:,2))
hold on
stairs(time,X(:,2),'r--')
grid on
xlabel('t [s]')
ylabel('x_3')
legend('x_{3s}','x_3')


%% Punto 6. Applicazione del controllo digitale all'impianto tempo-continuo
s = tf('s');
G = 5/(0.01*s+1)/(s^2/9+1.2/3*s+1);
sim('TfContinuousModel_OptimalControlIntegral_obs_mdl', Tfin)
figure(7)
set(7,'name','Sistema tempo-continuo con controllo ottimo integrale e osservatore tempo-discreti')
subplot(2,1,1)
plot(time,yd,'r:')
hold on
grid on
plot(time,y)
ylabel('y(t)')
xlabel('t')
legend('y_p','y')
subplot(2,1,2)
timed = 0:Ts:Tfin
stairs(timed,u)
grid on
ylabel('u(t)')
xlabel('t')