while true; do
    read -p "What do you like to do?
    1) init ES
    2) get ES credential
    3) get Kibana OTP
    4) open Kibana UI
    5) build service docker
    6) restart service
    7) open search page
    e) exit
" e1234567
    case $e1234567 in
        [1]* ) docker run --name es01 --net host -p 9200:9200 -it -m 1GB -d docker.elastic.co/elasticsearch/elasticsearch:8.17.2;
               docker run --name kib01 --net host -p 5601:5601 -d docker.elastic.co/kibana/kibana:8.17.2;;
        [2]* ) docker logs es01 | grep 'Password *' -A 10 -B 1;;
        [3]* ) docker logs kib01 | grep 'Your verification code is: ' -A 1 -B 1;;
        [4]* ) gio open http://localhost:5601;;
        [5]* ) docker build -f Dockerfile -t judgement-tool:1.0.0 .;;
        [6]* ) espwd=$(docker logs es01 | grep 'Password *' -A 1 | grep -v 'Password *' | xargs | sed -e 's/\x1b\[[0-9;]*m//g' | sed 's/[^a-zA-Z0-9=*+_-]//g');
               ssl=$(docker logs es01 | grep ' SHA-256 fingerprint:' -A 1 | grep -v ' SHA-256 *' | xargs | sed -e 's/\x1b\[[0-9;]*m//g'| sed 's/[^a-zA-Z0-9]//g');
               docker stop judgement-tool;
               docker rm judgement-tool;
               echo "Password: ${espwd}";
               echo "SSL: ${ssl}";
               docker run --env DATA_PATH=/usr/app/data --env ELASTICSEARCH_REST_PASSWORD=${espwd} --env ELASTICSEARCH_REST_SSL=${ssl} -v ./data:/usr/app/data -p 8080:8080 --net host -d --name judgement-tool judgement-tool:1.0.0;;
        [7]* ) open ./Search.html;;
        [e]* ) exit;;
        * ) echo "Not valid answer";;
    esac
done