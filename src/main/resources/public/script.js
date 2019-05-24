var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message_input_value');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

function connect() {
    var socket = new SockJS('/chat-messaging');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        console.log("connected: " + frame);
        stompClient.subscribe('/chat/messages', function(response) {
            console.log(response);
            console.log("+");
            var data = JSON.parse(response.body);
            draw("left", data.message);
            stompClient.send("/app/addUser", {}, JSON.stringify({name: $("#userName").val(), type: 'JOIN'}))
        });
    });
}

function draw(side, text) {
    console.log("drawing...");
    var $message;
    $message = $($('.message_template').clone().html());
    $message.addClass(side).find('.text').html(text);
    $('.messages').append($message);
    return setTimeout(function () {
        return $message.addClass('appeared');
    }, 0);
}
function disconnect(){
    stompClient.disconnect();
}
function getDataFromApi() {
    $.ajax({
        type:"GET",
        url:"/getdata",
        success:function (data) {
            console.log("GET id = " + data.id);
            console.log("GET name = " + data.name);
        },
        error:function (jqXHR,textStatus,errorThrown) {

        }
    });
}
function postDataFromApi() {
    var obj ={
        id:"",
        name:$("#userName").val()
    };
    console.log(obj);
    $.ajax({
        type:"POST",
        url:"/postdata",
        headers:{
            "Content-Type":"application/json",
            "Accept":"application/json"
        },
        data:JSON.stringify(obj),
        success:function (data) {
            console.log("POST = " + data);
        },
        error:function (jqXHR,textStatus,errorThrown) {

        }
    });
}
function sendMessage(){
    stompClient.send("/app/message", {}, JSON.stringify({'message': $("#message_input_value").val()}));
   //draw("left", $("#message_input_value").val());
}
