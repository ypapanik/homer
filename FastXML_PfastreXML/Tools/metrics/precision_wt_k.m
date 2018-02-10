function P = precision_wt_k(score_mat,true_mat,wts,K)
	num_inst = size(true_mat,2);
	num_lbl = size(true_mat,1);
	wt_true_mat = spdiags(wts,0,num_lbl,num_lbl)*true_mat;

	num = helper(score_mat,true_mat,wts,K);
	den = helper(wt_true_mat,true_mat,wts,K);
	P = num./den;
end

function P = helper(score_mat,true_mat,wts,K)
	num_inst = size(score_mat,2);
	num_lbl = size(score_mat,1);

	P = zeros(K,1);
	rank_mat = sort_sparse_mat(score_mat);

	for k=1:K
		mat = rank_mat;
%The 4 lines below are used because some sparse matrices are not converted correctly with mat(rank_mat>k) = 0;
		[i,j,v] = find(mat);
		[m,n] = size(mat);
		v(v>k) = 0;
		mat = sparse(i,j,v,m,n);

%		mat(rank_mat>k) = 0;
		mat = spones(mat);
		mat = spdiags(wts,0,num_lbl,num_lbl)*mat;
		mat = mat.*true_mat;
		num = sum(mat,1);

		P(k) = mean(num/k);
	end
end