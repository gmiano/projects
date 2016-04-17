%--------------------------------------------------------------------------
% Prova di esame Gennaio 2016
%--------------------------------------------------------------------------

% Variabili sistema internazionale
m = 1; Km = 1000*m; mm = 0.001*m; cm = 0.01*m;
Kg = 1; gr = 0.001*Kg; mg = 0.001*gr;
s = 1; minuti = 60*s; ora = 60*minuti;
g = 9.81*m/s^2; % Forza di gravità
Kgf = g; % Kilogrammiforza
V = 1;
rad = 1;
gradi = pi*rad/180;
rpm = 2*pi/60;
N = 1; Nm = N*m; mNm = 0.001*Nm;
Amp = 1; mAmp = 0.001*Amp;
Ohm = V/Amp; mOhm = 0.001*Ohm;
Henry = V*s/Amp; mHenry = 0.001*Henry;
% Fine definizione unità di misura del sistema internazionale

% Parametri esercizio
M = 5*Kg;
K_m = 5*N*(m^2)*Amp^-2;
K_0 = 4*N*m^2;
K_v = 1*V/m;
v_0 = 2*V;
% Fine definizione parametri esercizio

%%Punto 1
% Levitatore_NonLineare

%% Punto 2
x_e = [0.5 0]';
u_e=sqrt((M*g*x_e(1)^2-K_0)/K_m);
u_e
[X,Y,U,DX] = trim('Levitatore_NonLineare',[],u_e,[],[],[],[]);
i_e = sqrt((M*g*x_e(1)^2-K_0)/K_m);

