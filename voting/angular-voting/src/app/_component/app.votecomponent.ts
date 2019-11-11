import { Component, OnInit } from "@angular/core";
import { UserService } from "../_service/app.userservice";
import { Router } from "@angular/router";
import { Poll } from "../_model/app.poll";
import { User } from "../_model/app.user";
import { timer } from "rxjs";

@Component({
    selector:'vote',
    templateUrl:'../_html/app.vote.html'
})
export class VoteComponent implements OnInit{

    center:string;
    poll:Poll={
        pollCenter:null,
        startTime:null,
        endTime:null
    }
    currentTime:any;
    nominees:User;
    timer:any;

    constructor(private service:UserService, private router:Router){}

    ngOnInit(){
        this.service.searchUserPoll(sessionStorage.getItem("userEmail")).subscribe(
            (data:Poll)=>{this.poll=data;this.getStartTime();this.getEndTime();this.getNominees();this.getTimer(new Date(this.poll.endTime).getTime())});
        this.center=sessionStorage.getItem("location");
        this.currentTime = new Date().getTime();
        console.log(this.currentTime);
    }

    getStartTime(){
        this.poll.startTime = new Date(this.poll.startTime).getTime();
        console.log(this.poll.startTime);
    }

    getEndTime(){
        this.poll.endTime = new Date(this.poll.endTime).getTime();
        console.log(this.poll.endTime);
    }

    getNominees(){
        this.service.getCenterNominees(sessionStorage.getItem("location")).subscribe((data:User)=>{
            this.nominees=data;console.log(this.nominees);
        })
    }

    vote(id:number){
        this.service.vote(sessionStorage.getItem("userId"),id).subscribe((data)=>
            {alert("Voted Successfully!")}, error=>{alert(error.error)});
    }

    getTimer(endTime){
        let currentTime = new Date().getTime();
        //var countDownDate = new Date("Jan 5, 2021 15:37:25").getTime();

        // Update the count down every 1 second
        var x = setInterval(function() {

        // Find the distance between now and the count down date
        var distance = endTime - currentTime;
            
        // Time calculations for days, hours, minutes and seconds
        //var days = Math.floor(distance / (1000 * 60 * 60 * 24));
        var hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
        var seconds = Math.floor((distance % (1000 * 60)) / 1000);
            
        // Output the result in an element with id="demo"
        this.timer =  hours + "h "+ minutes + "m " + seconds + "s ";
        console.log(this.timer);
        // If the count down is over, write some text 
        if (distance < 0) {
            clearInterval(x);
        }
        }, 1000);
    }
}