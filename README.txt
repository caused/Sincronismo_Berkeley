README

java -jar sincronismo.jar -s ip=localhost:9997 time=12:04:00 logFile=C:\Users\galves\Sincronismo\dist\logA.txt

java -jar sincronismo.jar -s ip=ip=localhost:9996 time=12:08:00 logFile=C:\Users\galves\Sincronismo\dist\logB.txt

java -jar sincronismo.jar -s ip=localhost:9995 time=11:55:00 logFile=C:\Users\galves\Sincronismo\dist\logC.txt

java -jar sincronismo.jar -m ip=localhost:9998 time=12:01:00 d=250000000000000000 slavesFile=C:\Users\galves\Sincronismo\dist\slaves.txt logFile=C:\Users\galves\Sincronismo\dist\log.txt


Arquivos submetidos:

- 1 jar, podendo ser executado em cada uma das máquinas, passando os devidor parâmetros para se comportarem como master ou slave
---- Mestre,  com IP localhost:9998 - Master com d 250000000
---- IP localhost:9997 - Slave
---- IP localhost:9996 - Slave
---- IP localhost:9995 - Slave

Exemplos de utilização se encontram no começo do arquivo

-Amostra:
	--Logs de execução do servidor
	--print dos 4 terminais executando o jar
Sincronismo
	--Codigos fontes do projeto
Projeto:
	--Descrição dos objetivos do projeto


Dentro do Código fonte

Main - Classe principal para executar o algoritmo
ArgsHandler - Classe para tratar os parametros da linha de comando
IOHandler - Classe para tratar operações de IO com arquivos
SocketHandler - Classe para tratar operações de socket
TimeHandler - Classe para tratar operações com o tempo
Executor - Interface que define a operação padrão do programa
Master - Classe que irá executar as tarefas de mestre do tempo
Slave - Classe que ira executar as tarefas de escravo do tempo
IpAddress - Classe que representa um endereco de IP com sua respectiva porta
Log - Classe para lidar com exibição de mensagens para o usuário no console e no arquivo de log



