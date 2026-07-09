import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../service/auth.service';
import { WebSocketService } from '../../service/web-socket.service';
import { ParticipantService } from '../../service/participant.service';
import { Participant } from '../../models/particiapant';
import { ListComponent } from "../../components/lsit/list.component";

@Component({
  selector: 'app-main-page',
  standalone: true,
  templateUrl: './main-page.component.html',
  imports: [ListComponent]
})
export class MainPageComponent implements OnInit {
  private auth = inject(AuthService);
  private router = inject(Router);
  private ws = inject(WebSocketService);
  private service = inject(ParticipantService)
  private cdr = inject(ChangeDetectorRef);

participants: Participant[] = [];

  things: {status:string; participants: Participant[]}[]=[
    {status: 'WAITING', participants: []},
    {status: 'ONGOING', participants: []},
    {status: 'FINISHED', participants: []}
  ]

  ngOnInit() {
    this.load();
    this.ws.onMessage((msg) => {
      console.log('Received message:', msg);
      this.load();
    });
  }

  logout() {
    this.auth.logout().subscribe({
      next: () => {
        this.ws.disconnect();
        this.router.navigate(['/login']);
      }
    });
  }

  get user() {
    return this.auth.getUser();
  }

  load(){
    this.service.getAll().subscribe({
      next: (data)=> {
        this.participants = data;
        this.things.forEach((status)=>{
          status.participants = this.participants.filter((p)=> p.status === status.status)
        });
        this.cdr.markForCheck();
      }
    })
  }

  edit(data: { participant: Participant; score: number }){
    this.service.score(data.participant.id, data.score, this.user!.jury).subscribe({});
  }

  onReady(participant: Participant){
    this.service.ready(participant.id).subscribe({});
  }
}