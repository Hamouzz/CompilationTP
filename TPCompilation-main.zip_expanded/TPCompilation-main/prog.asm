DATA SEGMENT 
	a DD
	b DD
DATA ENDS 
CODE SEGMENT
		mov eax, 5
		push eax
		mov a, eax
		mov eax, 4
		push eax
		mov eax, a
		push eax
		pop ebx
		pop eax
		sub eax, ebx
		push eax
		jl etiq_debut_gt_2
		mov eax,0
		jmp etiq_fin_gt_2
	etiq_debut_gt_2:
		mov eax,1
	etiq_fin_gt_2:
		jz etiq_fin_and_1
		mov eax, 2
		push eax
		mov eax, a
		push eax
		pop ebx
		pop eax
		sub eax, ebx
		push eax
		jl etiq_debut_gt_3
		mov eax,0
		jmp etiq_fin_gt_3
	etiq_debut_gt_3:
		mov eax,1
	etiq_fin_gt_3:
	etiq_fin_and_1:
		jz etiq_if_sinon_0
		mov eax, a
		push eax
		jmp etiq_if_fin_0
	etiq_if_sinon_0:
		mov eax, 2
		push eax
	etiq_if_fin_0:
		mov b, eax
CODE ENDS