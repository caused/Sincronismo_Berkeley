README

java -jar sincronismo.jar -s ip=localhost:9997 time=12:04:00 logFile=C:\Users\galves\Sincronismo\dist\logA.txt

java -jar sincronismo.jar -s ip=ip=localhost:9996 time=12:08:00 logFile=C:\Users\galves\Sincronismo\dist\logB.txt

java -jar sincronismo.jar -s ip=localhost:9995 time=11:55:00 logFile=C:\Users\galves\Sincronismo\dist\logC.txt

java -jar sincronismo.jar -m ip=localhost:9998 time=12:01:00 d=250000000000000000 slavesFile=C:\Users\galves\Sincronismo\dist\slaves.txt logFile=C:\Users\galves\Sincronismo\dist\log.txt


Arquivos submetidos:

- 1 jar, podendo ser executado em cada uma das m�quinas, passando os devidor par�metros para se comportarem como master ou slave
---- Mestre,  com IP localhost:9998 - Master com d 250000000
---- IP localhost:9997 - Slave
---- IP localhost:9996 - Slave
---- IP localhost:9995 - Slave

Exemplos de utiliza��o se encontram no come�o do arquivo

-Amostra:
	--Logs de execu��o do servidor
	--print dos 4 terminais executando o jar
Sincronismo
	--Codigos fontes do projeto
Projeto:
	--Descri��o dos objetivos do projeto


Dentro do C�digo fonte

Main - Classe principal para executar o algoritmo
ArgsHandler - Classe para tratar os parametros da linha de comando
IOHandler - Classe para tratar opera��es de IO com arquivos
SocketHandler - Classe para tratar opera��es de socket
TimeHandler - Classe para tratar opera��es com o tempo
Executor - Interface que define a opera��o padr�o do programa
Master - Classe que ir� executar as tarefas de mestre do tempo
Slave - Classe que ira executar as tarefas de escravo do tempo
IpAddress - Classe que representa um endereco de IP com sua respectiva porta
Log - Classe para lidar com exibi��o de mensagens para o usu�rio no console e no arquivo de log



